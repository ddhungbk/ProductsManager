package bk.hungd.Extra;

import java.io.File;
import java.io.FileFilter;

public class ExtensionsFilter implements FileFilter {

	private String[] extensions;
	
	public ExtensionsFilter(String[] extensions) {
		this.extensions = extensions;
	}
	
	@Override
	public boolean accept(File file) {
		for(String extension : extensions){
			if(file.isFile() && file.getName().toLowerCase().endsWith(extension)){
				return true;
			}
		}
		return false;
	}

	
}
