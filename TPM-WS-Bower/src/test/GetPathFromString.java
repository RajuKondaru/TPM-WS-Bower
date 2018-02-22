package test;

public class GetPathFromString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pathContains="application: label='Amazon Shopping' icon='res/drawable/app_icon.png'";
		
		 System.out.println(pathContains.substring(pathContains.lastIndexOf("=") + 1));
		
	}

}
