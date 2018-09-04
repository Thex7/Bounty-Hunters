package com.gmail.srthex7.multicore.File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.EnumSet;

public class FileUtil {
	public static void copyOneFicher (String sourceFile, String destinationFile) {
		System.out.println("Desde: " + sourceFile);
		System.out.println("Hacia: " + destinationFile);

		try {
			File inFile = new File(sourceFile);
			File outFile = new File(destinationFile);

			FileInputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(outFile);

			int c;
			while( (c = in.read() ) != -1)
				out.write(c);

			in.close();
			out.close();
		} catch(IOException e) {
			System.err.println("Hubo un error de entrada/salida!!!");
		}
	}
	
	public static void copyall(String source,String path){
		Path sourcePath      = Paths.get(source);
		Path destinationPath = Paths.get(path);

		try {
		    Files.copy(sourcePath, destinationPath,StandardCopyOption.REPLACE_EXISTING);
		} catch(FileAlreadyExistsException e) {
		    //destination file already exists
		} catch (IOException e) {
		    //something else went wrong
		    e.printStackTrace();
		}
	}
	
	public static void delete(String path){
		Path s = Paths.get(path);
		    try {
				Files.deleteIfExists(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public static void copyDirectory(final String in, final String out, StandardCopyOption action)
	        throws IOException {
		
		Path source = Paths.get(in);
		Path target = Paths.get(out);
		System.out.println("In: " + in);
		System.out.println("Out: " +out);
	    Files.walkFileTree(source, EnumSet.of(FileVisitOption.FOLLOW_LINKS),
	        Integer.MAX_VALUE, new FileVisitor<Path>() {

	            @Override
	            public FileVisitResult preVisitDirectory(Path dir,
	                    BasicFileAttributes sourceBasic) throws IOException {
	                Path targetDir = Files.createDirectories(target
	                    .resolve(source.relativize(dir)));
	                AclFileAttributeView acl = Files.getFileAttributeView(dir,
	                    AclFileAttributeView.class);
	                if (acl != null)
	                    Files.getFileAttributeView(targetDir,
	                        AclFileAttributeView.class).setAcl(acl.getAcl());
	                DosFileAttributeView dosAttrs = Files.getFileAttributeView(
	                    dir, DosFileAttributeView.class);
	                if (dosAttrs != null) {
	                    DosFileAttributes sourceDosAttrs = dosAttrs
	                        .readAttributes();
	                    DosFileAttributeView targetDosAttrs = Files
	                        .getFileAttributeView(targetDir,
	                            DosFileAttributeView.class);
	                    targetDosAttrs.setArchive(sourceDosAttrs.isArchive());
	                    targetDosAttrs.setHidden(sourceDosAttrs.isHidden());
	                    targetDosAttrs.setReadOnly(sourceDosAttrs.isReadOnly());
	                    targetDosAttrs.setSystem(sourceDosAttrs.isSystem());
	                }
	                FileOwnerAttributeView ownerAttrs = Files
	                    .getFileAttributeView(dir, FileOwnerAttributeView.class);
	                if (ownerAttrs != null) {
	                    FileOwnerAttributeView targetOwner = Files
	                        .getFileAttributeView(targetDir,
	                            FileOwnerAttributeView.class);
	                    targetOwner.setOwner(ownerAttrs.getOwner());
	                }
	                PosixFileAttributeView posixAttrs = Files
	                    .getFileAttributeView(dir, PosixFileAttributeView.class);
	                if (posixAttrs != null) {
	                    PosixFileAttributes sourcePosix = posixAttrs
	                        .readAttributes();
	                    PosixFileAttributeView targetPosix = Files
	                        .getFileAttributeView(targetDir,
	                            PosixFileAttributeView.class);
	                    targetPosix.setPermissions(sourcePosix.permissions());
	                    targetPosix.setGroup(sourcePosix.group());
	                }
	                UserDefinedFileAttributeView userAttrs = Files
	                    .getFileAttributeView(dir,
	                        UserDefinedFileAttributeView.class);
	                if (userAttrs != null) {
	                    UserDefinedFileAttributeView targetUser = Files
	                        .getFileAttributeView(targetDir,
	                            UserDefinedFileAttributeView.class);
	                    for (String key : userAttrs.list()) {
	                        ByteBuffer buffer = ByteBuffer.allocate(userAttrs
	                            .size(key));
	                        userAttrs.read(key, buffer);
	                        buffer.flip();
	                        targetUser.write(key, buffer);
	                    }
	                }
	                // Must be done last, otherwise last-modified time may be
	                // wrong
	                BasicFileAttributeView targetBasic = Files
	                    .getFileAttributeView(targetDir,
	                        BasicFileAttributeView.class);
	                targetBasic.setTimes(sourceBasic.lastModifiedTime(),
	                    sourceBasic.lastAccessTime(),
	                    sourceBasic.creationTime());
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult visitFile(Path file,
	                    BasicFileAttributes attrs) throws IOException {
	                Files.copy(file, target.resolve(source.relativize(file)), action);
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult
	                    visitFileFailed(Path file, IOException e)
	                            throws IOException {
	                throw e;
	            }

	            @Override
	            public FileVisitResult postVisitDirectory(Path dir,
	                    IOException e) throws IOException {
	                if (e != null) throw e;
	                return FileVisitResult.CONTINUE;
	            }
	        });
	}
	
}