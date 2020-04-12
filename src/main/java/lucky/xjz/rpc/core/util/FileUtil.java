package lucky.xjz.rpc.core.util;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger("lucky/xjz/rpc/core");

    private FileUtil() {
    }

    /**
     * 从指定位置开始到指定位置结束读取文件的字节数据
     * @param fileUrl 文件路径
     * @param begin   开始读取文件的位置
     * @param length  结束读取文件的位置
     * @return 在规定区间内读取到的文件数据
     */
    public static byte[] readFile(String fileUrl, long begin, int length) {

        if (begin < 0) {
            log.info("read file start position must  more than 0 ");
            return null;
        }

        if (length <= 0) {
            log.info("read file length must  more than 0 ");
            return null;
        }

        if (!Files.exists(Paths.get(fileUrl))) {

            log.info(fileUrl + "文件不存在!");
            return null;
        }

        RandomAccessFile raf = null;
        FileChannel fileChannel = null;
        //可读字节数
        int readable = 0;
        try {
            //可以任意位置读取文件 只读
            raf = new RandomAccessFile(fileUrl, "r");
            //文件开始读取的位置
            raf.seek(begin);
            //获取读取文件的通道  双向 可以读可以写
            fileChannel = raf.getChannel();
            //初始化缓冲区大小
            ByteBuffer buffer = ByteBuffer.allocate(length);
            //填充缓冲区 返回填充字节的长度
            readable = fileChannel.read(buffer);
            //没有可读字节
            if (readable <= 0) {
                return null;
            }
            //状态的翻转  由写变成读 或者由读变成写
            buffer.flip();
            //初始化字节数组 长度为缓冲区剩余的长度
            byte[] content = new byte[buffer.remaining()];
            //填充字节数组
            //buffer.get(content, 0, content.length);
            buffer.get(content);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(raf);
            IOUtils.closeQuietly(fileChannel);
        }
        return null;
    }

    /**
     * 向文件中写数据 I
     * @param file
     * @param content
     * @param isAppend 覆盖|追加 true false
     * @return
     */
    public static boolean writeFile(File file, byte[] content, boolean isAppend) {

        if (content == null || content.length == 0) {
            log.info("content is not Effective");
            return false;
        }
        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file, isAppend);
                 FileChannel fileChannel = fos.getChannel()) {
                ByteBuffer buff = ByteBuffer.allocate(content.length);
                //填充缓冲区
                buff.put(content);
                //由读变为写 翻转通道
                buff.flip();
                //向文件中追加数据
                fileChannel.write(buff);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;

    }

    /**
     * 创建文件 创建顺序 父目录->创建文件
     * @param src 创建文件的文件路径
     * @return 创建的文件File对象
     */
    public static File createFile(String src) {
        File file = new File(src);
        if (!file.getParentFile().exists()) {

            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return file;
    }

    /**
     * 解压缩 用到了 org.apache.commons commons-compress 这个jar
     * @param zipFilePath   压缩文件源路径
     * @param targetDirPath 解压缩后文件存放的目录
     * @throws Exception
     */
    public static void uncompressZip(String zipFilePath, String targetDirPath) throws Exception {

        Long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ZipArchiveInputStream zipInputStream = null;

        try {
            if (!"zip".equals(zipFilePath.substring(zipFilePath.length() - 3))) {

                throw new Exception(zipFilePath + " 不是ZIP文件");
            }
            if (!Files.exists(Paths.get(zipFilePath))) {

                throw new Exception("路径 " + zipFilePath + " 不存在!");
            }
            //创建文件输出目录
            Files.createDirectories(Paths.get(targetDirPath));
            //文件输入流
            inputStream = new FileInputStream(zipFilePath);
            //zip 输入流
            zipInputStream = new ZipArchiveInputStream(inputStream, "UTF-8");
            ZipEntry zipEntry = null;

            while (null != (zipEntry = zipInputStream.getNextZipEntry())) {
                //判断是否是目录
                if (zipEntry.isDirectory()) {
                    //先创建目录
                    Files.createDirectories(Paths.get(targetDirPath, zipEntry.getName()));
                } else {
                    File file = createFile(targetDirPath + File.separator + zipEntry.getName());
                    outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    IOUtils.copy(zipInputStream, outputStream);
                }
            }
            Long consumeTime = System.currentTimeMillis() - startTime;
            log.info("压缩文件花费时间:{}", consumeTime);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(zipInputStream);
        }
    }

    /**
     * 创建空的zip文件
     * @param url
     * @return
     */
    public static File createEmptyZip(String url) {

        File file = createFile(url + ".zip");
        ZipOutputStream zipOutputStream = null;
        if (file != null) {
            FileOutputStream fOutputStream = null;
            try {
                fOutputStream = new FileOutputStream(file);
                zipOutputStream = new ZipOutputStream(fOutputStream);
                return file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (zipOutputStream != null) {
                        zipOutputStream.close();
                    }
                    if (fOutputStream != null) {
                        fOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }


}
