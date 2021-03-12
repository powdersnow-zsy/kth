package cn.kth.common.util;


import cn.kth.common.conf.ConfProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class HdfsUtils {

    public static String uri = null;

    static {
        uri = ConfProperties.getStringValue("hdfs.uri");
    }

    private static ThreadLocal<HdfsUtils> tts = new ThreadLocal<HdfsUtils>() {

        @Override
        protected HdfsUtils initialValue() {
            return new HdfsUtils();
        }
    };

    public static HdfsUtils getInstance() {
        return tts.get();
    }

    /**
     * make a new dir in the hdfs
     *
     * @param dir the dir may like '/tmp/testdir'
     * @return boolean true-success, false-failed
     * @throws IOException something wrong happends when operating files
     */
    public boolean mkdir(String dir) throws IOException {
        if (StringUtil.isBlank(dir)) {
            return false;
        }
        dir = formatHdfsPath(dir);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        if (!fs.exists(new Path(dir))) {
            fs.mkdirs(new Path(dir));
        }

        fs.close();
        return true;
    }

    /**
     * delete a dir in the hdfs. if dir not exists, it will throw FileNotFoundException
     *
     * @param dir the dir may like '/tmp/testdir'
     * @return boolean true-success, false-failed
     * @throws IOException something wrong happends when operating files
     */
    public boolean deleteDir(String dir) throws IOException {
        if (StringUtil.isBlank(dir)) {
            return false;
        }
        dir = formatHdfsPath(dir);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        fs.delete(new Path(dir), true);
        fs.close();
        return true;
    }

    /*
     * upload the local file to the hds, notice that the path is full like /tmp/test.txt if local file not exists, it
     * will throw a FileNotFoundException
     * @param localFile local file path, may like F:/test.txt or /usr/local/test.txt
     * @param hdfsFile hdfs file path, may like /tmp/dir
     * @return boolean true-success, false-failed
     * @throws IOException file io exception
     */
    public List<String> listAll(String dir) throws IOException {
        if (StringUtil.isBlank(dir)) {
            return new ArrayList<String>();
        }
        dir = formatHdfsPath(dir);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        FileStatus[] stats = fs.listStatus(new Path(dir));

        List<String> names = new ArrayList<String>();
        for (int i = 0; i < stats.length; ++i) {
            if (stats[i].isFile()) {
                // dir
                names.add(stats[i].getPath().toString());
            } else {
                // regular file
                names.add(stats[i].getPath().toString());
            }
        }

        fs.close();
        return names;
    }

    public List<String> listAllFileName(String dir) throws IOException {
        if (StringUtil.isBlank(dir)) {
            return new ArrayList<String>();
        }
        dir = formatHdfsPath(dir);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        FileStatus[] stats = fs.listStatus(new Path(dir));

        List<String> names = new ArrayList<String>();
        for (int i = 0; i < stats.length; ++i) {
            if (stats[i].isFile()) {
                // dir
                names.add(stats[i].getPath().getName());
            } else {
                // regular file
                names.add(stats[i].getPath().getName());
            }
        }

        fs.close();
        return names;
    }

    /*
     * upload the local file to the hds, notice that the path is full like /tmp/test.txt if local file not exists, it
     * will throw a FileNotFoundException
     * @param localFile local file path, may like F:/test.txt or /usr/local/test.txt
     * @param hdfsFile hdfs file path, may like /tmp/dir
     * @return boolean true-success, false-failed
     * @throws IOException file io exception
     */
    public boolean uploadLocalFile2HDFS(String localFile, String hdfsFile) throws IOException {
        if (StringUtil.isBlank(localFile) || StringUtil.isBlank(hdfsFile)) {
            return false;
        }
        hdfsFile = formatHdfsPath(hdfsFile);
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(uri), config);
        Path src = new Path(localFile);
        Path dst = new Path(hdfsFile);
        hdfs.copyFromLocalFile(src, dst);
        hdfs.close();
        return true;
    }

    public static void download(String remote, String local) {
        try {
            Path path = new Path(remote);
            Configuration config = new Configuration();
            FileSystem hdfs = FileSystem.get(URI.create(uri), config);

            hdfs.copyToLocalFile(path, new Path(local));

            hdfs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * create a new file in the hdfs. if dir not exists, it will create one
     *
     * @param newFile new file path, a full path name, may like '/tmp/test.txt'
     * @param content file content
     * @return boolean true-success, false-failed
     * @throws IOException file io exception
     */
    public boolean createNewHDFSFile(String newFile, String content) throws IOException {
        if (StringUtil.isBlank(newFile) || null == content) {
            return false;
        }
        newFile = formatHdfsPath(newFile);
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(newFile), config);
        FSDataOutputStream os = hdfs.create(new Path(newFile));
        os.write(content.getBytes("UTF-8"));
        os.close();
        hdfs.close();
        return true;
    }

    /**
     * create a new file in the hdfs. if dir not exists, it will create one
     *
     * @param newFile new file path, a full path name, may like '/tmp/test.txt'
     * @param content file content
     * @return boolean true-success, false-failed
     * @throws IOException file io exception
     */
    public boolean createNewHDFSFile(String newFile, byte[] content) throws IOException {
        if (StringUtil.isBlank(newFile) || null == content) {
            return false;
        }
        newFile = formatHdfsPath(newFile);
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(newFile), config);
        FSDataOutputStream os = hdfs.create(new Path(newFile));
        os.write(content);
        os.close();
        hdfs.close();
        return true;
    }

    /**
     * delete the hdfs file
     *
     * @param hdfsFile a full path name, may like '/tmp/test.txt'
     * @return boolean true-success, false-failed
     * @throws IOException file io exception
     */
    public boolean deleteHDFSFile(String hdfsFile) throws IOException {
        if (StringUtil.isBlank(hdfsFile)) {
            return false;
        }
        hdfsFile = formatHdfsPath(hdfsFile);
        Configuration config = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(hdfsFile), config);
        Path path = new Path(hdfsFile);
        boolean isDeleted = hdfs.delete(path, true);
        hdfs.close();
        return isDeleted;
    }

    /**
     * read the hdfs file content
     *
     * @param hdfsFile a full path name, may like '/tmp/test.txt'
     * @return byte[] file content
     * @throws IOException file io exception
     */
    public byte[] readHDFSFile(String hdfsFile) throws Exception {
        if (StringUtil.isBlank(hdfsFile)) {
            return null;
        }
        hdfsFile = formatHdfsPath(hdfsFile);

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(hdfsFile), conf);
        // check if the file exists
        Path path = new Path(hdfsFile);
        if (fs.exists(path)) {
            FSDataInputStream is = fs.open(path);
            // get the file info to create the buffer
            FileStatus stat = fs.getFileStatus(path);
            // create the buffer
            byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
            is.readFully(0, buffer);
            is.close();
            fs.close();
            return buffer;
        } else {
            throw new Exception("the file is not found .");
        }
    }

    public static String formatHdfsPath(String hdfsPath) {
        if (!hdfsPath.startsWith(uri)) {
            hdfsPath = uri + hdfsPath;
        }
        return hdfsPath;
    }

    /**
     * append something to file dst
     *
     * @param hdfsFile a full path name, may like '/tmp/test.txt'
     * @param content  string
     * @return boolean true-success, false-failed
     * @throws Exception something wrong
     */
    public boolean append(String hdfsFile, String content) throws Exception {
        if (StringUtil.isBlank(hdfsFile)) {
            return false;
        }
        if (StringUtil.isEmpty(content)) {
            return true;
        }

        hdfsFile = formatHdfsPath(hdfsFile);
        Configuration conf = new Configuration();
        // solve the problem when appending at single datanode hadoop env
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        FileSystem fs = FileSystem.get(URI.create(hdfsFile), conf);
        // check if the file exists
        Path path = new Path(hdfsFile);
        if (fs.exists(path)) {
            try {
                InputStream in = new ByteArrayInputStream(content.getBytes());
                OutputStream out = fs.append(new Path(hdfsFile));
                IOUtils.copyBytes(in, out, 8192, true);
                out.close();
                in.close();
                fs.close();
            } catch (Exception ex) {
                fs.close();
                throw ex;
            }
        } else {
            createNewHDFSFile(hdfsFile, content);
        }
        return true;
    }

    public boolean exist(String hdfsPath) throws IOException {
        if (StringUtil.isBlank(hdfsPath)) {
            return false;
        }

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(hdfsPath);
        return fs.exists(path);
    }

    public boolean isDirectory(String dir) throws IOException {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(URI.create(uri), configuration);
        Path path = new Path(dir);
        return fileSystem.isDirectory(path);
    }

    public boolean rename(String dir, String dir1) {
        Configuration configuration = new Configuration();
        try (FileSystem fileSystem = FileSystem.get(URI.create(uri), configuration)) {
            Path path = new Path(dir);
            Path path1 = new Path(dir1);
            fileSystem.rename(path, path1);
        } catch (IOException e) {
            log.error("rename error dir={}, dir1={}", dir, dir1, e);
            return false;
        }

        return true;
    }

    public long getFileModificationTime(String dir) {
        Configuration configuration = new Configuration();
        try (FileSystem fileSystem = FileSystem.get(URI.create(uri), configuration)) {
            Path path = new Path(dir);

            long modificationTime = fileSystem.getFileStatus(path).getModificationTime();

            FileStatus[] statuses = fileSystem.listStatus(path);
            for (FileStatus status : statuses) {
                modificationTime = Math.max(modificationTime, status.getModificationTime());
            }

            return modificationTime;
        } catch (IOException e) {
            log.error("getFileModificationTime error, dir={}", dir);
        }
        return 0L;
    }

}
