package forProgramJudge.testFiles;

import forProgramJudge.testFiles.tools.Tools;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haogs
 */
public class BeforeJudge {
    Tools tools=new Tools();
    public static void main(String[] args) {
        //第一步，把所有的压缩文件拷贝到指定路径
        BeforeJudge fc = new BeforeJudge();
        String[] fileType = {".rar", ".zip"};
        //origin:两个班级，两个文件夹，所以是数组
        //continue:可以把两个班级的两个文件夹放到同一个文件夹中，再进行传递
        String startFileString = "E:\\hao\\Fangcloud of JSNUV2\\个人文件\\job\\courses\\2019\\java数据库";
        String destinationFile = "E:\\dataset";
        for (String fileType1 : fileType) {
                fc.fileMove(startFileString, destinationFile, fileType1.toLowerCase());
        }
        //第二步，解压压缩文件。
        //解压命令："c:\Program Files\WinRAR\Rar.exe" x  s178334131ABC.rar。该命令会把文件解压到压缩文件所在目录
        //分两步走：
        //2.1 解压最外层的压缩文件到文件夹；//这一步可以到文件下，选中所有文件，右键，然后用rar解压所有文件到当前路径
        ///2.2 再搜索文件，遇到压缩文件，就解压。
        for (String fileType1 : fileType) {
            for (int i = 0; i < 10; i++) {
                fc.fileExtract(fileType1, startFileString);
            }
        }
    }
    
    public void fileMove(String startDirection, String destination, String fileType) {
        File path = new File(startDirection);
        List<File> files = tools.getFiles(path);

        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getName().toLowerCase().endsWith(fileType)) {
                files.get(i).renameTo(new File(destination + "\\" + files.get(i).getName()));
            }
        }
    }

    public void fileExtract(String fileType, String filesPath) {
        String comdString = "\"c:\\Program Files\\WinRAR\\Rar.exe\" x ";
        List<File> files =tools.getFiles(new File(filesPath));
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getName().toLowerCase().endsWith(fileType)) {
                try {
                    String individualPath = files.get(i).getPath();
                    Runtime.getRuntime().exec(comdString + files.get(i).getPath() + " " + individualPath.substring(0, individualPath.lastIndexOf("\\")));
                } catch (IOException ex) {
                }
            }
        }
    }
}
