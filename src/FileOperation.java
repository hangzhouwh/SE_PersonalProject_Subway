import java.io.*;

/**
 * @Author: hangzhouwh
 * @DATE: 2019/10/6
 * @MAIL: hangzhouwh@gmail.com
 */
public class FileOperation {
    public static void writeFile(String text, String outFilePath) throws IOException {
        StringReader in = new StringReader(text);
        // 以StringReader流为底层流
        BufferedReader reader = new BufferedReader(in);
        FileWriter out = new FileWriter(outFilePath);
        BufferedWriter writer = new BufferedWriter(out);
        String str;
        // 读一行数据，并返回该行内容字符串
        while((str = reader.readLine()) != null ){
            // 显示该行数据
            System.out.println(str);
            // 将该行数据写入缓冲输出流（间接写入底层流的文件）
            writer.write(str);
            // 输出行分隔符，达到按行输出的效果
            writer.newLine();
        }
        reader.close();
        writer.close();
    }
}
