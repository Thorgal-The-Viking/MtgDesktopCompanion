package org.magic.api.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.magic.api.exports.impl.JsonExport;
import org.magic.tools.FileTools;
import org.magic.tools.ImageTools;

import com.google.common.io.Files;

public class GedEntry <T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String ext;
	private byte[] content;
	private boolean isImage;
	private Icon icon;
	private Class<T> classe;
	private String object;
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public String getExt() {
		return ext;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setIsImage(boolean isImage) {
		this.isImage = isImage;
	}
	
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	public void setObject(T object) {
		this.object = new JsonExport().toJson(object);
	}
	
	public T getObject() {
		return new JsonExport().fromJson(object, classe);
	}
	
	
	public GedEntry(File f,Class<T> classe) throws IOException {
		this.classe=classe;
		setName(Files.getNameWithoutExtension(f.getName()));
		setExt(Files.getFileExtension(f.getName()));
		setContent(Files.toByteArray(f));
		setIsImage(ImageTools.isImage(f));
		setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
		setId(FileTools.checksum(f));
	}

	public Class<T> getClasse() {
		return classe;
	}
	
	public boolean isImage()
	{
		return isImage;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFullName() {
		return getName()+"."+getExt();
	}

}
