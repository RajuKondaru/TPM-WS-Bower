package test;

public class strtest {

	public static void main(String[] args) {
		StringBuffer reqUrl=new StringBuffer("http://localhost:8080/TestPaceMobile/ws/hub/ghfdhfghd");
		reqUrl= reqUrl.replace(0, 43, "http://localhost:4723/wd/hub");
		System.out.println(reqUrl);

	}

}
