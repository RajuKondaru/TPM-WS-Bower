package test;

import java.io.File;
import java.io.IOException;

public class Main {
  
  /**
   * Creates a new and empty directory in the default temp directory using the
   * given prefix. This methods uses {@link File#createTempFile} to create a
   * new tmp file, deletes it and creates a directory for it instead.
   * 
   * @param prefix The prefix string to be used in generating the diretory's
   * name; must be at least three characters long.
   * @return A newly-created empty directory.
   * @throws IOException If no directory could be created.
   */
  public static File createTempDir(String prefix)
    throws IOException
  {
    String tmpDirStr = System.getProperty("java.io.tmpdir");
    if (tmpDirStr == null) {
      throw new IOException(
        "System property 'java.io.tmpdir' does not specify a tmp dir");
    }
    
    File tmpDir = new File(tmpDirStr);
    if (!tmpDir.exists()) {
      boolean created = tmpDir.mkdirs();
      if (!created) {
        throw new IOException("Unable to create tmp dir " + tmpDir);
      }
    }
    
    File resultDir = null;
    int suffix = (int)System.currentTimeMillis();
    int failureCount = 0;
    do {
      resultDir = new File(tmpDir, prefix + suffix % 10000);
      suffix++;
      failureCount++;
    }
    while (resultDir.exists() && failureCount < 50);
    
    if (resultDir.exists()) {
      throw new IOException(failureCount + 
        " attempts to generate a non-existent directory name failed, giving up");
    }
    boolean created = resultDir.mkdir();
    if (!created) {
      throw new IOException("Failed to create tmp directory");
    }
    
    return resultDir;
  }
  public static void main(String[] args) throws IOException {
	  File temp = File.createTempFile("folder-name","");
	  temp.delete();
	  temp.mkdir();
}
}