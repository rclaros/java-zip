package org.pentafile.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author rclaros
 */
public class MainZip {

    List<String> fileList = new ArrayList<String>();
    String OUTPUT_ZIP_FILE = null;
    String SOURCE_FOLDER = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String help = "\n\n"
                + "Use :\n\n"
                + "   java -jar java-zip.jar \"path\" \"file.zip\" \n\n"
                + "Definition :\n"
                + "    path        - Absolute directory path \n\n"
                + "    file.zip    - Name file zip ";
        if (args.length > 0) {
            MainZip appZip = new MainZip();
            appZip.SOURCE_FOLDER = args[0];
            appZip.OUTPUT_ZIP_FILE = args[1];
            appZip.generateFileList(new File(appZip.SOURCE_FOLDER));
            appZip.zipIt(appZip.OUTPUT_ZIP_FILE);
        } else {
            System.out.println(help);
        }
    }

    /**
     *
     * @param zipFile
     */
    public void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            System.out.println("Output to Zip : " + zipFile);
            for (String file : this.fileList) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
            }
            zos.closeEntry();
            zos.close();
            System.out.println("Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param node
     */
    public void generateFileList(File node) {

        if (node.isFile()) {
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
        }
        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    /**
     *
     * @param file
     * @return
     */
    private String generateZipEntry(String file) {
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }

}
