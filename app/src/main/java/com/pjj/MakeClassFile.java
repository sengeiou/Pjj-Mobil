package com.pjj;

import com.google.gson.JsonObject;
import com.pjj.utils.JsonUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by xinheng on 2018/10/19。
 * describe：mvp 模式生成器
 */
public class MakeClassFile {
    private static String rootPath;
    private String appPackagePath;
    private String packageName;
    private String date;
    private String author;

    public static void main(String[] args) {
        //makeString(320, 480);
//        MakeClassFile makeClassFile = new MakeClassFile();
//        makeClassFile.make("xinheng");
//        makeClassFile.makePackage("contract");
        //build/intermediates/javac/debug/compileDebugJavaWithJavac/classes
        String jwd = "{'paths': [[[120.39656027769345, 30.383331082643405],[120.3965536102999, 30.383317743819486],[120.39652360728377, 30.383261053056764],[120.39647526939113, 30.383174348374226],[120.39641192980072, 30.38305929815339],[120.39632858820347, 30.382910899071884],[120.39623024526884, 30.382742489081906],[120.39611856753717, 30.38255573576372],[120.39599272093953, 30.38234397062944],[120.39586687361687, 30.382132204887643]],[[120.3804899922109, 30.356220771609028],[120.38044161967252, 30.3561107025377],[120.38040825890423, 30.356030654179705],[120.38036822531397, 30.355927261565615],[120.38035154461043, 30.3558839034728],[120.3803365317455, 30.355842214076535],[120.38031818331108, 30.35579885420513],[120.38030150285462, 30.35575882997137],[120.3802864902398, 30.355720474436573]],[[120.29578399315305, 30.098338266916635],[120.29576730516098, 30.098264909027826],[120.29575562449625, 30.098224892001067],[120.29574561230712, 30.098188210710134],[120.29573059321342, 30.09812318864037],[120.29568219618243, 30.097886450529533]]],'spatialReference': {'wkid': 4326}}";
        JsonObject jsonObject = JsonUtils.toJsonObject(jwd);
        jwd=JsonUtils.toJsonString(jsonObject);
        System.out.println(jwd);
    }

    public void make(String author) {
        this.author = author;
        URL resource = getClass().getResource("");
        String path = resource.getPath();
        //D:\studioSpace\company\tjj\app\src\main\java\com\tlw\tjj\MakeClassFile.java
        path = path.replace("build/intermediates/javac/debug/compileDebugJavaWithJavac/classes", "src/main/java");
        path = path.substring(1, path.length());
        rootPath = path;
        String str = "/main/java/";
        int i = path.indexOf(str);
        if (i < 0) {
            throw new RuntimeException("路径错误");
        }
        appPackagePath = path.substring(i + str.length(), path.length());
        packageName = appPackagePath.replace("/", ".");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        date = sf.format(new Date());
        System.out.println(path);
        System.out.println(appPackagePath);
        //makeActivitySystemClass("ScreenManageOrderList", "屏幕管理订单列表");
        makeActivitySystemClass("MyRelease", "我的发布");
    }

    public void makePackages() {
        makePackage("contract");
        makePackage("intent");
        makePackage("module");
        makePackage("present");
        makePackage("receiver");
        makePackage("service");
        makePackage("utils");
        makePackage("view/activity");
        makePackage("view/custom");
        makePackage("view/fragment");
    }

    public void makeBaseClass() {
        createContract("Base");
        createActivity("Base", false, null);
        createFragment("Base", false, null);
        createPresent("Base");
    }

    public void makeActivitySystemClass(String name, String explain) {
        createContract(name);
        createPresent(name);
        createActivity(name, true, explain);
    }

    public void makeFragmentSystemClass(String name, String explain) {
        createContract(name);
        createPresent(name);
        createFragment(name, true, explain);
    }

    public void makeCustomView(String name, String parent, String explain) {
        String namePath = "view/custom/";
        String className = name + "View.java";
        String name1 = name + "View";
        writeContent(rootPath + namePath + className, createCustomView(name1, parent, explain));
        String xmlName = "attrs.xml";
        int index = rootPath.indexOf("java/" + appPackagePath);
        String resPath = rootPath.substring(0, index) + "res/";
        System.out.println(resPath);
        String layoutPath = resPath + "values/";
        System.out.println(layoutPath);
        writeContent(layoutPath + xmlName, "    <declare-styleable name=\"" + name1 + "\">\n    </declare-styleable>\n", true);
    }

    public void makePackage(String packageName) {
        File file = new File(rootPath + packageName);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void createActivity(String name, boolean createXml, String explain) {
        String namePath = "view/activity/";
        String activityName = name + "Activity.java";
        writeContent(rootPath + namePath + activityName, createViewContent(name, "Activity", explain));
        if (createXml && !name.contains("Base")) {
            String xmlName = "activity_" + name.toLowerCase() + ".xml";
            int index = rootPath.indexOf("java/" + appPackagePath);
            String resPath = rootPath.substring(0, index) + "res/";
            System.out.println(resPath);
            String layoutPath = resPath + "layout/";
            System.out.println(layoutPath);
            writeContent(layoutPath + xmlName, createXmlContent(getPathForPoint(namePath, name + "Activity")));
        }
    }

    private void createFragment(String name, boolean createXml, String explain) {
        String namePath = "view/fragment/";
        String activityName = name + "Fragment.java";
        writeContent(rootPath + namePath + activityName, createViewContent(name, "Fragment", explain));
        if (createXml && !name.contains("Base")) {
            String xmlName = "fragment_" + name.toLowerCase() + ".xml";
            int index = rootPath.indexOf("java/" + appPackagePath);
            String resPath = rootPath.substring(0, index) + "res/";
            System.out.println(resPath);
            String layoutPath = resPath + "layout/";
            System.out.println(layoutPath);
            writeContent(layoutPath + xmlName, createXmlContent(getPathForPoint(namePath, name + "Fragment")));
        }
    }

    private void createContract(String name) {
        makePackage("contract");
        String path = rootPath + "contract/";
        String fileName;
        if (name.contains("Base")) {
            fileName = "BaseView";
            writeContent(path + fileName + ".java", createBaseView());
        } else {
            fileName = name + "Contract";
            writeContent(path + fileName + ".java", createContractContent(name));
        }
    }

    private void createPresent(String name) {
        makePackage("present");
        String path = rootPath + "present/";
        String fileName = name + "Present";
        String classPath = path + fileName + ".java";
        if (name.equals("Base")) {
            writeContent(classPath, createBasePresentContent());
        } else {
            writeContent(classPath, createPresentContent(name));
        }
    }

    private String getPathForPoint(String namePath, String name) {
        return "." + namePath.replace("/", ".") + name;
    }

    private String createViewContent(String name, String type, String explain) {
        if (name.contains("Base")) {
            return createBaseActivityContent();
        }
        String classFrontName;
        String parent;
        String addContent;
        String bundle = "";
        if (type.equals("Activity")) {
            //public class InactiveActivity extends BaseActivity<InactivePresent> implements InactiveContract.View {
            classFrontName = "view.activity";
            parent = "BaseActivity";
            addContent = "    @Override\n" +
                    "    protected void onCreate(Bundle savedInstanceState) {\n" +
                    "        super.onCreate(savedInstanceState);\n" +
                    "        setContentView(R.layout.activity_" + name.toLowerCase() + ");\n" +
                    "    }\n\n";
            bundle = "import android.os.Bundle;\n\n";
        } else {
            classFrontName = "view.fragment";
            parent = "BaseFragment";
            addContent = "    @Override\n" +
                    "    protected int getLayoutRes() {\n" +
                    "        return R.layout.fragment_" + name.toLowerCase() + ";\n" +
                    "    }\n";
        }
        StringBuffer buffer = new StringBuffer();
        //包名
        buffer.append("package ");
        buffer.append(packageName);
        buffer.append(classFrontName);
        buffer.append(";\n");
        buffer.append("\n");
        buffer.append(bundle);
        buffer.append("import com.pjj.R;\n");
        buffer.append("import com.pjj.contract.");
        buffer.append(name);
        buffer.append("Contract;\n");
        buffer.append("import com.pjj.present.");
        buffer.append(name);
        buffer.append("Present;\n");

        addExplain(buffer, explain);

        buffer.append("public class ");
        buffer.append(name);
        buffer.append(type);
        buffer.append(" extends ");
        buffer.append(parent);
        buffer.append("<");
        buffer.append(name);
        buffer.append("Present> implements ");
        buffer.append(name);
        buffer.append("Contract.View {\n");
        buffer.append("\n");
        buffer.append(addContent);
        buffer.append("}\n");
        return buffer.toString();
    }

    private String createContractContent(String name) {
        StringBuffer buffer = new StringBuffer();
        //包名
        buffer.append("package ");
        buffer.append(packageName);
        buffer.append("contract;\n");
        buffer.append("\n");
        buffer.append("/**\n * Create by ");
        buffer.append(author);
        buffer.append(" on ");
        buffer.append(date);
        buffer.append("。\n * describe：\n");
        buffer.append(" */\n");
        buffer.append("public interface ");
        buffer.append(name);
        buffer.append("Contract {\n");
        buffer.append("    interface View extends BaseView {\n");
        buffer.append("\n");
        buffer.append("    }\n\n");
        buffer.append("    interface Present {\n\n");
        buffer.append("    }\n}");
        return buffer.toString();

    }

    private String createPresentContent(String name) {
        StringBuffer buffer = new StringBuffer();
        addPackageName(buffer, "present");

        buffer.append("import ");
        buffer.append(packageName);
        buffer.append("contract.");
        buffer.append(name);
        buffer.append("Contract;\n");

        addExplain(buffer, "P层");
        buffer.append("public class ");
        buffer.append(name);
        buffer.append("Present extends BasePresent<");
        buffer.append(name);
        buffer.append("Contract.View> implements ");
        buffer.append(name);
        buffer.append("Contract.Present {\n\n");
        buffer.append("    public ");
        buffer.append(name);
        buffer.append("Present(");
        buffer.append(name);
        buffer.append("Contract.View view) {\n");
        buffer.append("        super(view, ");
        buffer.append(name);
        buffer.append("Contract.View.class);\n");
        buffer.append("    }\n");
        buffer.append("}\n");
        return buffer.toString();
    }

    private String createBasePresentContent() {
        StringBuffer buffer = new StringBuffer();
        addPackageName(buffer, "present");
        buffer.append("import ");
        buffer.append(packageName);
        buffer.append("contract.BaseView;\n");
        buffer.append("import java.lang.reflect.InvocationHandler;\n");
        buffer.append("import java.lang.reflect.Method;\n");
        buffer.append("import java.lang.reflect.Proxy;\n");
        addExplain(buffer, null);
        buffer.append("public class BasePresent<V extends BaseView> {\n");
        buffer.append("    protected V mView;\n");
        buffer.append("    private ViewInvocationHandler invocationHandler;\n");
        buffer.append("\n");
        buffer.append("    public BasePresent(V view, Class<V> vClass) {\n");
        buffer.append("        invocationHandler = new ViewInvocationHandler(view);\n");
        buffer.append("        mView = (V) Proxy.newProxyInstance(vClass.getClassLoader(), new Class[]{vClass}, invocationHandler);\n");
        buffer.append("    }\n");
        buffer.append("\n");
        buffer.append("    public void recycle() {\n");
        buffer.append("        if (null != invocationHandler) {\n");
        buffer.append("            invocationHandler.recycle();\n");
        buffer.append("            invocationHandler = null;\n");
        buffer.append("        }\n");
        buffer.append("        mView = null;\n");
        buffer.append("    }\n");
        buffer.append("\n");
        buffer.append("    public boolean isEffective() {\n");
        buffer.append("        return null != mView;\n");
        buffer.append("    }\n");
        buffer.append("\n");
        buffer.append("    private class ViewInvocationHandler implements InvocationHandler {\n");
        buffer.append("        private V view;\n");
        buffer.append("\n");
        buffer.append("        public ViewInvocationHandler(V view) {\n");
        buffer.append("            this.view = view;\n");
        buffer.append("        }\n");
        buffer.append("\n");
        buffer.append("        @Override\n");
        buffer.append("        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {\n");
        buffer.append("            if (null != view) {\n");
        buffer.append("                try {\n");
        buffer.append("                    return method.invoke(view, args);\n");
        buffer.append("                } catch (InvocationTargetException e) {\n");
        buffer.append("                    throw e.getCause();\n");
        buffer.append("                }\n");
        buffer.append("            }");
        buffer.append("            return null;\n");
        buffer.append("        }\n");
        buffer.append("\n");
        buffer.append("        public void recycle() {\n");
        buffer.append("            view = null;\n");
        buffer.append("        }\n");
        buffer.append("    }\n");
        buffer.append("}");

        return buffer.toString();
    }

    private String createXmlContent(String classPath) {//.view.activity.MainActivity
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        buffer.append("<android.support.constraint.ConstraintLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n");
        buffer.append("    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n");
        buffer.append("    xmlns:tools=\"http://schemas.android.com/tools\"\n");
        buffer.append("    android:layout_width=\"match_parent\"\n");
        buffer.append("    android:layout_height=\"match_parent\"\n");
        buffer.append("    tools:context=\"");
        buffer.append(classPath);
        buffer.append("\">\n");
        buffer.append("\n");
        buffer.append("    <TextView\n");
        buffer.append("        android:layout_width=\"wrap_content\"\n");
        buffer.append("        android:layout_height=\"wrap_content\"\n");
        buffer.append("        android:text=\"Hello World!\"\n");
        buffer.append("        app:layout_constraintBottom_toBottomOf=\"parent\"\n");
        buffer.append("        app:layout_constraintLeft_toLeftOf=\"parent\"\n");
        buffer.append("        app:layout_constraintRight_toRightOf=\"parent\"\n");
        buffer.append("        app:layout_constraintTop_toTopOf=\"parent\" />\n");
        buffer.append("\n");
        buffer.append("</android.support.constraint.ConstraintLayout>");
        return buffer.toString();
    }

    private String createBaseView() {
        StringBuffer buffer = new StringBuffer();
        //包名
        buffer.append("package ");
        buffer.append(packageName);
        buffer.append("contract;\n");
        buffer.append("\n");
        buffer.append("/**\n * Create by ");
        buffer.append(author);
        buffer.append(" on ");
        buffer.append(date);
        buffer.append("。\n * describe：view 基础协议\n");
        buffer.append(" */\n");
        buffer.append("public interface BaseView {\n");
        buffer.append("    /**\n");
        buffer.append("     * 展示等待状态\n");
        buffer.append("     */\n");
        buffer.append("    void showWaiteStatue();\n");
        buffer.append("\n");
        buffer.append("    /**\n");
        buffer.append("     * 取消等待状态\n");
        buffer.append("     */\n");
        buffer.append("    void cancelWaiteStatue();\n");
        buffer.append("\n");
        buffer.append("    /**\n");
        buffer.append("     * 失败状态\n");
        buffer.append("     * @param error 原因\n");
        buffer.append("     */\n");
        buffer.append("    void showNotice(String error);\n");
        buffer.append("}");
        return buffer.toString();
    }

    private String createBaseActivityContent() {//2018/10/16
        StringBuffer buffer = new StringBuffer();
        buffer.append("package " + packageName + "view.activity;\n\n");
        buffer.append("import android.os.Bundle;\n");
        buffer.append("import android.support.v7.app.AppCompatActivity;\n");
        buffer.append("import android.widget.Toast;\n\n");
        buffer.append("import com.pjj.present.BasePresent;\n");
        addExplain(buffer, null);
        buffer.append("public class BaseActivity<P extends BasePresent> extends AppCompatActivity {\n");
        buffer.append("    protected P mPresent;\n");
        buffer.append("    private Toast mToast;\n");
        buffer.append("\n");
        buffer.append("    @Override\n");
        buffer.append("    protected void onCreate(Bundle savedInstanceState) {\n");
        buffer.append("        super.onCreate(savedInstanceState);\n");
        buffer.append("    }\n");
        buffer.append("\n");
        buffer.append("    protected void toast(String msg) {\n");
        buffer.append("        if (mToast == null) {\n");
        buffer.append("            mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);\n");
        buffer.append("        } else {\n");
        buffer.append("            mToast.setText(msg);\n");
        buffer.append("        }\n");
        buffer.append("        mToast.show();\n");
        buffer.append("    }\n");
        buffer.append("\n");
        buffer.append("    @Override\n");
        buffer.append("    protected void onPause() {\n");
        buffer.append("        if (null != mToast) {\n");
        buffer.append("            mToast.cancel();\n");
        buffer.append("        }\n");
        buffer.append("        super.onPause();\n");
        buffer.append("    }\n");
        buffer.append("\n");
        buffer.append("    @Override\n");
        buffer.append("    protected void onDestroy() {\n");
        buffer.append("        mPresent.recycle();\n");
        buffer.append("        mPresent = null;\n");
        buffer.append("        mToast = null;\n");
        buffer.append("        super.onDestroy();\n");
        buffer.append("    }\n");
        buffer.append("}");
        return buffer.toString();
    }

    private String createCustomView(String name, String patent, String explaint) {
        StringBuffer buffer = new StringBuffer();
        addPackageName(buffer, "view.custom");
        buffer.append("import android.content.Context;\n");
        buffer.append("import android.content.res.TypedArray;\n");
        buffer.append("import android.util.AttributeSet;\n");
        addExplain(buffer, explaint);
        //buffer.append("public class TitleView extends View {");
        buffer.append("public class ");
        buffer.append(name);
        buffer.append(" extends ");
        buffer.append(patent);
        buffer.append(" {\n");

        //buffer.append("    public TitleView(Context context) {");
        buffer.append("    public ");
        buffer.append(name);
        buffer.append("(Context context) {\n");
        buffer.append("        this(context, null);\n    }\n\n");
        buffer.append("    public ");
        buffer.append(name);
        buffer.append("(Context context, AttributeSet attrs) {\n");
        buffer.append("        this(context, attrs, 0);\n    }\n\n");
        buffer.append("    public ");
        buffer.append(name);
        buffer.append("(Context context, AttributeSet attrs, int defStyle) {\n");
        buffer.append("        super(context, attrs, defStyle);\n");
        buffer.append("        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.");
        buffer.append(name);
        buffer.append(", defStyle, 0);\n");
        buffer.append("        int indexCount = typedArray.getIndexCount();\n");
        buffer.append("        for (int i = 0; i < indexCount; i++) {\n");
        buffer.append("            int index = typedArray.getIndex(i);\n");
        buffer.append("            switch (index) {\n");
        buffer.append("                case R.styleable.");
        buffer.append(name);
        buffer.append("_:\n");
        buffer.append("                    break;\n            }\n        }\n");
        buffer.append("        typedArray.recycle();\n    }\n");
        buffer.append("}\n");
        return buffer.toString();
    }

    private void addPackageName(StringBuffer buffer, String classPackage) {
        if (null == buffer) {
            return;
        }
        if (null == classPackage) {
            classPackage = "*";
        }
        buffer.append("package ");
        buffer.append(packageName);
        buffer.append(classPackage);
        buffer.append(";\n");
        buffer.append("\n");
    }

    private void addExplain(StringBuffer buffer, String explainText) {
        if (null == explainText) {
            explainText = "";
        }
        buffer.append("\n");
        buffer.append("/**\n * Create by ");
        buffer.append(author);
        buffer.append(" on ");
        buffer.append(date);
        buffer.append("。\n * describe：");
        buffer.append(explainText);
        buffer.append("\n");
        buffer.append(" */\n");
    }

    private void writeContent(String path, String content) {
        writeContent(path, content, false);
    }

    private void writeContent(String path, String content, boolean append) {
        //BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(path,false));
        if (new File(path).exists()) {
            return;
        }
        System.out.println(path);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(path, append));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != printWriter) {
            printWriter.print(content);
            printWriter.flush();
            printWriter.close();
        }
    }
}
