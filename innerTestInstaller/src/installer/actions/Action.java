package installer.actions;

import installer.Activator;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.jface.dialogs.MessageDialog;

import java.io.BufferedReader;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException; 
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 

import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList; 
import org.xml.sax.SAXException; 



/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class Action implements IWorkbenchWindowActionDelegate {
	public static String[] jarNameString = {
		"resources/asm-all-4.2.jar",
		"resources/commons-logging-1.1.1.jar",
		"resources/innerTest.jar",
		"resources/javassist.jar",
		"resources/junit.jar",
		};
	
	
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public Action() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		IProject proj=getCurrentProject();
		
		System.out.println(proj.getLocation());
		IFile classpathFile=proj.getFile(".classpath");
		String classpathString=classpathFile.getLocation().toString();
		System.out.println(classpathString);

		changeClassPath(classpathString);
		
        MessageDialog.openInformation(
			window.getShell(),
			"Installer",
			"Finish build classpath for current project, please refresh the project by right click");
  } 

		
	public static void changeClassPath(String fileName) {
	    try{		   
		   	File inFile = new File(fileName);
	        File outFile = File.createTempFile(fileName, ".tmp");  
	     
	        FileInputStream fis = new FileInputStream(inFile);  
	        BufferedReader in = new BufferedReader(new InputStreamReader(fis));  
	     
	        FileOutputStream fos = new FileOutputStream(outFile);  
	        PrintWriter out = new PrintWriter(fos);  
	     
	        String thisLine;  
	        while ((thisLine = in.readLine()) != null) {  
	          if (thisLine.startsWith("</classpath>")) {  
	        	for (String jar:jarNameString){
	        		//System.out.println(Activator.getDefault().getBundle().getLocation());
	        		URL url = Activator.getDefault().getBundle().getResource(jar);
	        		System.out.println(url);
	        		System.out.println(FileLocator.toFileURL(url));
	    		
	        		String jarPath=FileLocator.toFileURL(url).toString();
	        		out.println("	<classpathentry kind=\"lib\" path=\""+jarPath.substring(5)+"\"/>");  
	        	}
	          }
	          out.println(thisLine);  
	        }  
	        out.flush();  
	        out.close();  
	        in.close();  
	     
	        inFile.delete();  
	        outFile.renameTo(inFile);  
	    	}
	    catch (IOException e) {
           e.printStackTrace();
        }
	  }


	public static IProject getCurrentProject(){    
        ISelectionService selectionService =     
            Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    
    
        ISelection selection = selectionService.getSelection();    
    
        IProject project = null;    
        if(selection instanceof IStructuredSelection) {    
            Object element = ((IStructuredSelection)selection).getFirstElement();    
    
            if (element instanceof IResource) {    
                project= ((IResource)element).getProject();    
            } else if (element instanceof PackageFragmentRootContainer) {    
                IJavaProject jProject =     
                    ((PackageFragmentRootContainer)element).getJavaProject();    
                project = jProject.getProject();    
            } else if (element instanceof IJavaElement) {    
                IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
                project = jProject.getProject();    
            }    
        }     
        return project;    
    }    
	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}