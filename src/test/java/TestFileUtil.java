import static lucky.xjz.rpc.core.util.FileUtil.uncompressZip;

public class TestFileUtil {


    public static void main(String[] args) {

        String src = "E:\\软件安全下载目录\\JAVA书籍\\JAVA书籍.zip";
        String target = "E:\\软件安全下载目录\\JAVA书籍\\testunzip";
        try {
            uncompressZip(src, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
