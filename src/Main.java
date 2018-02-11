import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    /**
     * 开始
     */
    public void begin() {
        //获取小说主目录
        File file = new File("金庸小说");
        //获取目录下面的所有文件
        File[] files = file.listFiles();
        List<String> names = new ArrayList<>();
        //对每个文件进行循环获取所有的书名
        for (int i = 0; i < files.length; i++) {
            //获取文件的名称
            String name = files[i].getName();
            //用xxx人物名单来街区xxx ,及书名
            if (name.contains("人物名单")) {
                //提取书名
                String bookName = name.substring(0, name.length() - 8);
                //添加到书名列表
                names.add(bookName);
            }
        }
        //开始读取对话
        for (int i = 0; i < names.size(); i++) {
            //根据书名读取相应文件
            String name = names.get(i);
            loadTxt(name);
        }
    }

    /**
     * 加载小说
     * @param name
     */
    public void loadTxt(String name) {
        //获取小说中人物列表
        List<String> peoples = loadPeople(name);
        //获取小说正文文件路径
        String fileName = "金庸小说/" + name + ".txt";
        try {
            //按行读取
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
            String str = null;//存储每一行的内容
            while ((str = bufferedReader.readLine()) != null) {
                //正则表达式,匹配到对话的内容
                Pattern pattern = Pattern.compile(".{0,20}\\“[^\\“\\”]+\\”");
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    //找到含有引号的对话的内容
                    String result = matcher.group();
                    for (int i = 0; i < peoples.size(); i++) {
                        //循环人物列表,判断是否是属于该人物的对话
                        String peopleName = peoples.get(i);
                        if (result.contains(peopleName)) {
                            //检索其中的内容
                            pattern = Pattern.compile("\\“[^\\“\\”]+\\”");
                            matcher = pattern.matcher(str);
                            String contract = "";
                            if (matcher.find()) {
                                //寻找到对话内容
                                contract = matcher.group();
                                System.out.println("单独对话:" + contract);
                            }
                            //去除首位符号
                            String realContract = contract.substring(1, contract.length() - 2);
                            //写入文件
                            File file = new File(name);
                            if (!file.exists()) {
                                file.mkdir();
                            }
                            String peopleFileName =name+ "/含" + peopleName + "的句子.txt";
                            BufferedWriter writer = new BufferedWriter(new FileWriter(peopleFileName,true));
                            writer.write(realContract);
                            writer.newLine();
                            writer.flush();
                        }
                    }
                    System.out.println(matcher.group());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回小说人物列表
     * @param name
     * @return
     */
    public List<String> loadPeople(String name) {
        String fileName = "金庸小说/" + name + "人物名单.txt";
        List<String> result = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                result.add(str);
                System.out.println(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 应用运行的入口
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.begin();
    }
}
