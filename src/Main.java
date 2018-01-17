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
        //读取所有文件名称
        File file = new File("金庸小说");
        File[] files = file.listFiles();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            if (name.contains("人物名单")) {
                //提取书名
                String bookName = name.substring(0, name.length() - 8);
                System.out.println(bookName);
                names.add(bookName);
            }
        }
        //开始读取对话
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            loadTxt(name);
        }
    }

    /**
     * 加载小说
     * @param name
     */
    public void loadTxt(String name) {
        //获取小说人物列表
        List<String> peoples = loadPeople(name);
        //读取小说的每一行
        String fileName = "金庸小说/" + name + ".txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK"));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                System.out.println("------------------------------");
                System.out.println("行内容:" + str);
                //获取到每一行
                Pattern pattern = Pattern.compile(".{0,20}\\“[^\\“\\”]+\\”");
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    String result = matcher.group();
                    System.out.println("匹配的内容:" + result);
                    for (int i = 0; i < peoples.size(); i++) {
                        String peopleName = peoples.get(i);
                        if (result.contains(peopleName)) {
                            //检索其中的对话
                            pattern = Pattern.compile("\\“[^\\“\\”]+\\”");
                            matcher = pattern.matcher(str);
                            String contract = "";
                            if (matcher.find()) {
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
                    System.out.println("--------------------------------");
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

    public static void main(String[] args) {
        Main main = new Main();
        main.begin();
    }
}
