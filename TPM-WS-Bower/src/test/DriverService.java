package test;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.Beta;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.os.CommandLine;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages the life and death of a native executable driver server.
 *
 * It is expected that the driver server implements the
 * <a href="https://github.com/SeleniumHQ/selenium/wiki/JsonWireProtocol">WebDriver Wire Protocol</a>.
 * In particular, it should implement /status command that is used to check if the server is alive.
 * In addition to this, it is supposed that the driver server implements /shutdown hook that is
 * used to stop the server.
 */
public class DriverService {
  /**
   * The base URL for the managed server.
   */
  private final URL url;

  /**
   * Controls access to {@link #process}.
   */
  private final ReentrantLock lock = new ReentrantLock();

  /**
   * A reference to the current child process. Will be {@code null} whenever this service is not
   * running. Protected by {@link #lock}.
   */
  private CommandLine process = null;

  private final String executable;
  private final ImmutableList<String> args;
  private final ImmutableMap<String, String> environment;

  /**
  *
  * @param executable The driver executable.
  * @param port Which port to start the driver server on.
  * @param args The arguments to the launched server.
  * @param environment The environment for the launched server.
  * @throws IOException If an I/O error occurs.
  */
 protected DriverService(File executable, int port, ImmutableList<String> args,
     ImmutableMap<String, String> environment) throws IOException {
   this.executable = executable.getCanonicalPath();
   url = new URL(String.format("http://localhost:%d", port));
   this.args = args;
   this.environment = environment;
 }

  /**
   * @return The base URL for the managed driver server.
   */
  public URL getUrl() {
    return url;
  }

  /**
   *
   * @param exeName Name of the executable file to look for in PATH
   * @param exeProperty Name of a system property that specifies the path to the executable file
   * @param exeDocs The link to the driver documentation page
   * @param exeDownload The link to the driver download page
   *
   * @return The driver executable as a {@link File} object
   * @throws IllegalStateException If the executable not found or cannot be executed
   */
  protected static File findExecutable(String exeName, String exeProperty, String exeDocs,
      String exeDownload) {
    String defaultPath = CommandLine.find(exeName);
    String exePath = System.getProperty(exeProperty, defaultPath);
    checkState(exePath != null,
        "The path to the driver executable must be set by the %s system property;"
            + " for more information, see %s. "
            + "The latest version can be downloaded from %s",
            exeProperty, exeDocs, exeDownload);

    File exe = new File(exePath);
    checkExecutable(exe);
    return exe;
  }

  protected static void checkExecutable(File exe) {
    checkState(exe.exists(),
        "The driver executable does not exist: %s", exe.getAbsolutePath());
    checkState(!exe.isDirectory(),
        "The driver executable is a directory: %s", exe.getAbsolutePath());
    checkState(FileHandler.canExecute(exe),
        "The driver is not executable: %s", exe.getAbsolutePath());
  }

  /**
   * Checks whether the driver child process is currently running.
   *
   * @return Whether the driver child process is still running.
   */
  public boolean isRunning() {
    lock.lock();
    try {
      if (process == null) {
        return false;
      }
      return process.isRunning();
    } catch (IllegalThreadStateException e) {
      return true;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Starts this service if it is not already running. This method will block until the server has
   * been fully started and is ready to handle commands.
   *
   * @throws IOException If an error occurs while spawning the child process.
   * @see #stop()
   */
  public CommandLine start() throws IOException {
    lock.lock();
    try {
      if (process != null) {
        return process;
      }
      process = new CommandLine(this.executable, args.toArray(new String[] {}));
      process.setEnvironmentVariables(environment);
      process.copyOutputTo(System.err);
      process.executeAsync();

      waitUntilAvailable();
      return process;
    } finally {
      lock.unlock();
    }
  }

  protected void waitUntilAvailable() throws MalformedURLException {
    try {
      URL status = new URL(url.toString() + "/status");
      new UrlChecker().waitUntilAvailable(20, SECONDS, status);
    } catch (UrlChecker.TimeoutException e) {
      process.checkForError();
      throw new WebDriverException("Timed out waiting for driver server to start.", e);
    }
  }

  /**
   * Stops this service is it is currently running. This method will attempt to block until the
   * server has been fully shutdown.
   *
   * @see #start()
   */
  public void stop() {
    lock.lock();
    try {
      if (process == null) {
        return;
      }
      URL killUrl = new URL(url.toString() + "/shutdown");
      new UrlChecker().waitUntilUnavailable(3, SECONDS, killUrl);
      process.destroy();
    } catch (MalformedURLException e) {
      throw new WebDriverException(e);
    } catch (UrlChecker.TimeoutException e) {
      throw new WebDriverException("Timed out waiting for driver server to shutdown.", e);
    } finally {
      process = null;
      lock.unlock();
    }
  }

  public static abstract class Builder<DS extends DriverService, B extends Builder> {

    private int port = 0;
    private File exe = null;
    private ImmutableMap<String, String> environment = ImmutableMap.of();
    private File logFile;

    /**
     * Sets which driver executable the builder will use.
     *
     * @param file The executable to use.
     * @return A self reference.
     */
    public B usingDriverExecutable(File file) {
      checkNotNull(file);
      checkExecutable(file);
      this.exe = file;
      return (B) this;
    }

    /**
     * Sets which port the driver server should be started on. A value of 0 indicates that any
     * free port may be used.
     *
     * @param port The port to use; must be non-negative.
     * @return A self reference.
     */
    public B usingPort(int port) {
      checkArgument(port >= 0, "Invalid port number: %s", port);
      this.port = port;
      return (B) this;
    }

    protected int getPort() {
      return port;
    }

    /**
     * Configures the driver server to start on any available port.
     *
     * @return A self reference.
     */
    public B usingAnyFreePort() {
      this.port = 0;
      return (B) this;
    }

    /**
     * Defines the environment for the launched driver server. These
     * settings will be inherited by every browser session launched by the
     * server.
     *
     * @param environment A map of the environment variables to launch the
     *     server with.
     * @return A self reference.
     */
    @Beta
    public B withEnvironment(Map<String, String> environment) {
      this.environment = ImmutableMap.copyOf(environment);
      return (B) this;
    }

    /**
     * Configures the driver server to write log to the given file.
     *
     * @param logFile A file to write log to.
     * @return A self reference.
     */
    public B withLogFile(File logFile) {
      this.logFile = logFile;
      return (B) this;
    }

    protected File getLogFile() {
      return logFile;
    }

    /**
     * Creates a new service to manage the driver server. Before creating a new service, the
     * builder will find a port for the server to listen to.
     *
     * @return The new service object.
     */
    public DS build() {
      if (port == 0) {
        port = PortProber.findFreePort();
      }

      if (exe == null) {
        exe = findDefaultExecutable();
      }

      ImmutableList<String> args = createArgs();

      return createDriverService(exe, port, args, environment);
    }

    protected abstract File findDefaultExecutable();

    protected abstract ImmutableList<String> createArgs();

    protected abstract DS createDriverService(File exe, int port, ImmutableList<String> args,
        ImmutableMap<String, String> environment);
  }
}
