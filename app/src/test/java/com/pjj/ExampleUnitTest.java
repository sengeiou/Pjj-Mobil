package com.pjj;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pjj.intent.ApiService;
import com.pjj.module.WeatherBean;
import com.pjj.utils.JsonUtils;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void parseJson() {
        String jwd = "{'paths': [[[120.39656027769345, 30.383331082643405],[120.3965536102999, 30.383317743819486],[120.39652360728377, 30.383261053056764],[120.39647526939113, 30.383174348374226],[120.39641192980072, 30.38305929815339],[120.39632858820347, 30.382910899071884],[120.39623024526884, 30.382742489081906],[120.39611856753717, 30.38255573576372],[120.39599272093953, 30.38234397062944],[120.39586687361687, 30.382132204887643]],[[120.3804899922109, 30.356220771609028],[120.38044161967252, 30.3561107025377],[120.38040825890423, 30.356030654179705],[120.38036822531397, 30.355927261565615],[120.38035154461043, 30.3558839034728],[120.3803365317455, 30.355842214076535],[120.38031818331108, 30.35579885420513],[120.38030150285462, 30.35575882997137],[120.3802864902398, 30.355720474436573]],[[120.29578399315305, 30.098338266916635],[120.29576730516098, 30.098264909027826],[120.29575562449625, 30.098224892001067],[120.29574561230712, 30.098188210710134],[120.29573059321342, 30.09812318864037],[120.29568219618243, 30.097886450529533]]],'spatialReference': {'wkid': 4326}}";
        String jw1 = "{'paths': [[[121.39656027769345, 32.383331082643409],[121.39655361029990,32.383317743819489],[121.39652360728377, 32.383261053056764],[121.39647526939113, 32.383174348374226],[121.39641192980072,32.383059298153391],[121.39632858820347,32.382910899071888],[121.39623024526884,32.382742489081906],[121.39611856753717,32.382555735763717],[121.39599272093953,32.382343970629435],[121.39586687361687,32.382132204887640]],[[121.38048999221090,32.356220771609031],[121.38044161967252,32.356110702537698],[121.38040825890423,32.356030654179705],[121.38036822531397,32.355927261565611],[121.38035154461043,32.355883903472801],[121.38033653174550,32.355842214076532],[121.38031818331108,32.355798854205133],[121.38030150285462,32.355758829971371],[121.38028649023980,32.355720474436573]],[[121.29578399315305,32.098338266916635],[121.29576730516098,32.098264909027826],[121.29575562449625,32.098224892001070],[121.29574561230712,32.098188210710134],[121.29573059321342,32.098123188640372],[121.29568219618243,32.097886450529529]]],'spatialReference ':{' wkid ':4326}}";
        JsonObject jsonObject = JsonUtils.toJsonObject(jwd);
        jwd = JsonUtils.toJsonString(jsonObject);
        System.out.println(jwd);
        JsonArray jsonArrayPaths = jsonObject.getAsJsonArray("paths");
        for (int i = 0; i < jsonArrayPaths.size(); i++) {
            JsonArray childJsonArrays = jsonArrayPaths.get(i).getAsJsonArray();
            //String s = childJsonArrays.toString();
            //System.out.println(s);
            for (int j = 0; j < childJsonArrays.size(); j++) {
                JsonArray childs = childJsonArrays.get(j).getAsJsonArray();
                double child1 = childs.get(0).getAsDouble();
                double child2 = childs.get(1).getAsDouble();
                System.out.println("child1=" + child1 + ", child2=" + child2);
                double[] doubles = gps84_To_Gcj02(child1, child2);
                childs.set(0, new JsonPrimitive(new BigDecimal(doubles[0], new MathContext(17, RoundingMode.HALF_EVEN))));
                childs.set(1, new JsonPrimitive(new BigDecimal(doubles[1], new MathContext(17, RoundingMode.HALF_EVEN))));
            }
        }
        jwd = JsonUtils.toJsonString(jsonObject);
        System.out.println(jwd);
    }

    private double[] gps84_To_Gcj02(double chidld1, double child2) {
        //随便计算的
        return new double[]{chidld1 + 1, child2 + 2};
    }

    @Test
    public void compare() {
        String s1 = "child1=120.39656027769345, child2=30.383331082643405\n" +
                "child1=120.3965536102999, child2=30.383317743819486\n" +
                "child1=120.39652360728377, child2=30.383261053056764\n" +
                "child1=120.39647526939113, child2=30.383174348374226\n" +
                "child1=120.39641192980072, child2=30.38305929815339\n" +
                "child1=120.39632858820347, child2=30.382910899071884\n" +
                "child1=120.39623024526884, child2=30.382742489081906\n" +
                "child1=120.39611856753717, child2=30.38255573576372\n" +
                "child1=120.39599272093953, child2=30.38234397062944\n" +
                "child1=120.39586687361687, child2=30.382132204887643\n" +
                "child1=120.3804899922109, child2=30.356220771609028\n" +
                "child1=120.38044161967252, child2=30.3561107025377\n" +
                "child1=120.38040825890423, child2=30.356030654179705\n" +
                "child1=120.38036822531397, child2=30.355927261565615\n" +
                "child1=120.38035154461043, child2=30.3558839034728\n" +
                "child1=120.3803365317455, child2=30.355842214076535\n" +
                "child1=120.38031818331108, child2=30.35579885420513\n" +
                "child1=120.38030150285462, child2=30.35575882997137\n" +
                "child1=120.3802864902398, child2=30.355720474436573\n" +
                "child1=120.29578399315305, child2=30.098338266916635\n" +
                "child1=120.29576730516098, child2=30.098264909027826\n" +
                "child1=120.29575562449625, child2=30.098224892001067\n" +
                "child1=120.29574561230712, child2=30.098188210710134\n" +
                "child1=120.29573059321342, child2=30.09812318864037\n" +
                "child1=120.29568219618243, child2=30.097886450529533";
        String s2 = "child1=120.39656027769345, child2=30.383331082643405\n" +
                "child1=120.3965536102999, child2=30.383317743819486\n" +
                "child1=120.39652360728377, child2=30.383261053056764\n" +
                "child1=120.39647526939113, child2=30.383174348374226\n" +
                "child1=120.39641192980072, child2=30.38305929815339\n" +
                "child1=120.39632858820347, child2=30.382910899071884\n" +
                "child1=120.39623024526884, child2=30.382742489081906\n" +
                "child1=120.39611856753717, child2=30.38255573576372\n" +
                "child1=120.39599272093953, child2=30.38234397062944\n" +
                "child1=120.39586687361687, child2=30.382132204887643\n" +
                "child1=120.3804899922109, child2=30.356220771609028\n" +
                "child1=120.38044161967252, child2=30.3561107025377\n" +
                "child1=120.38040825890423, child2=30.356030654179705\n" +
                "child1=120.38036822531397, child2=30.355927261565615\n" +
                "child1=120.38035154461043, child2=30.3558839034728\n" +
                "child1=120.3803365317455, child2=30.355842214076535\n" +
                "child1=120.38031818331108, child2=30.35579885420513\n" +
                "child1=120.38030150285462, child2=30.35575882997137\n" +
                "child1=120.3802864902398, child2=30.355720474436573\n" +
                "child1=120.29578399315305, child2=30.098338266916635\n" +
                "child1=120.29576730516098, child2=30.098264909027826\n" +
                "child1=120.29575562449625, child2=30.098224892001067\n" +
                "child1=120.29574561230712, child2=30.098188210710134\n" +
                "child1=120.29573059321342, child2=30.09812318864037\n" +
                "child1=120.29568219618243, child2=30.097886450529533";
        System.out.println(s1.equals(s2));
    }

    @Test
    public void textWeather() {
        String json = "{\"result\":{\"realtime\":{\"wind\":{\"windspeed\":\"\",\"direct\":\"西南风\",\"power\":\"2级\",\"offset\":\"\"},\"time\":\"15:00:00\",\"weather\":{\"humidity\":\"12\",\"img\":\"00\",\"info\":\"晴\",\"temperature\":\"28\"},\"dataUptime\":\"1559285542\",\"date\":\"2019-05-31\",\"city_code\":\"101010100\",\"city_name\":\"北京\",\"week\":\"5\",\"moon\":\"四月廿七\"},\"life\":{\"date\":\"2019-05-31\",\"info\":{\"kongtiao\":[\"部分时间开启\",\"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。\"],\"yundong\":[\"较适宜\",\"天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意防风。\"],\"ziwaixian\":[\"强\",\"紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。\"],\"ganmao\":[\"少发\",\"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。\"],\"xiche\":[\"较适宜\",\"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。\"],\"wuran\":null,\"chuanyi\":[\"热\",\"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。\"]}},\"weather\":[{\"date\":\"2019-05-31\",\"week\":\"五\",\"nongli\":\"四月廿七\",\"info\":{\"dawn\":[\"0\",\"晴\",\"15\",\"北风\",\"微风\",\"19:34\"],\"day\":[\"0\",\"晴\",\"31\",\"南风\",\"3-5级\",\"04:49\"],\"night\":[\"1\",\"多云\",\"19\",\"西南风\",\"3-5级\",\"19:35\"]}},{\"date\":\"2019-06-01\",\"week\":\"六\",\"nongli\":\"四月廿八\",\"info\":{\"dawn\":[\"1\",\"多云\",\"19\",\"西南风\",\"3-5级\",\"19:35\"],\"day\":[\"1\",\"多云\",\"32\",\"南风\",\"微风\",\"04:49\"],\"night\":[\"1\",\"多云\",\"19\",\"东北风\",\"微风\",\"19:36\"]}},{\"date\":\"2019-06-02\",\"week\":\"日\",\"nongli\":\"四月廿九\",\"info\":{\"dawn\":[\"1\",\"多云\",\"19\",\"东北风\",\"微风\",\"19:36\"],\"day\":[\"1\",\"多云\",\"33\",\"北风\",\"微风\",\"04:48\"],\"night\":[\"0\",\"晴\",\"19\",\"北风\",\"微风\",\"19:36\"]}},{\"date\":\"2019-06-03\",\"week\":\"一\",\"nongli\":\"五月初一\",\"info\":{\"dawn\":[\"0\",\"晴\",\"19\",\"北风\",\"微风\",\"19:36\"],\"day\":[\"1\",\"多云\",\"33\",\"北风\",\"微风\",\"04:48\"],\"night\":[\"1\",\"多云\",\"20\",\"东北风\",\"微风\",\"19:37\"]}},{\"date\":\"2019-06-04\",\"week\":\"二\",\"nongli\":\"五月初二\",\"info\":{\"dawn\":[\"1\",\"多云\",\"20\",\"东北风\",\"微风\",\"19:37\"],\"day\":[\"4\",\"雷阵雨\",\"30\",\"东风\",\"微风\",\"04:48\"],\"night\":[\"1\",\"多云\",\"19\",\"北风\",\"3-5级\",\"19:38\"]}}],\"pm25\":{\"key\":\"北京\",\"show_desc\":null,\"pm25\":{\"curPm\":\"31\",\"pm25\":\"6\",\"pm10\":\"\",\"level\":\"1\",\"quality\":\"优\",\"des\":\"空气很棒，快出门呼吸新鲜空气吧。\"},\"dateTime\":\"2019年05月31日14时\",\"cityName\":\"北京\"},\"isForeign\":0},\"error_code\":0,\"reason\":\"Succes\"}";
        WeatherBean parse = JsonUtils.parse(json, WeatherBean.class);
        WeatherBean.ResultBeanX.RealtimeBean realtime = parse.getResult().getRealtime();
        System.out.println(realtime.getCity_name());
    }

    @Test
    public void addition_isCorrect() throws InterruptedException {
        String[] split = ",14,16,".split(",");
        System.out.println(split.length);
        System.out.println(split);
        //String s = AESUtils.desEncrypt("P/PVhI/INseiFqUySoz/3jodR83tu+sbnjSXvGzD69YWGYM/ueBhAdNTbHBqq9f+IP2VMRSLStDL\nJFP8gjCANU2exbGJSGbd0A5IT6sXk7+Ky3eaH5cpNU3mD2CcnBnayDhH29tAinryPWWo7t7hhzQh\nUJbvj65yqHCos5HAINU1iottQpL6Em6tL31WVkDSAvSFOa7izqa86R3wV0uhnv/oiNIEm48JQXX7\nIfVe5PZCRAC+PhXQu7oCWH2r2GhV6D5WmO+UFm5HGLQPkHfhFEXy/2feiFosgCxI60lQljYBBnAu\n7Bd7DS0/pXa2D10fVbJI6t4mK/+5L7A7MpaHow5YC0gafgcjB5DGEn68Q6alTZaZ7P/XSMjUbIAl\n/pYFvRMlMkDtHHdKaIBRBBNOziLpjb1TzdcO8f49tlCZmEHA8P88KmTCKcy25Al5IqztfrHQTpPQ\nChJbEbMIhgTZMHSWcsCXQx7KCf7RjDpDrutHHH/soNgymAJ2aLid8YurPfAgnvvJqQzx0li5bBXp\nCwgrDjApNPUhcqx+lEMq7M553jvuWEJbHUKELSro71gUyNJB+jGx0za788DKYoexlMqmDNoeoVrd\nmWm4EHo0Em1shl4jLw4rYE0EVVdMc4xlF9hMTInQrR590I2AhaDCkwzBEl88pbdFf2MDLix8jU72\nt9tr85H7wGKjIfzm9/7BRiG9fVUmhtwxSir4e5llizhD7BXsGpRGlm385BGhab4Jzbz7jsQVlsZC\n7qe3uehBbd+6kFiQksjpTV6TagS8HkAkIHQUqYfrAdZjFTc91kdt1sdxK51C4SoSakAMT/H/sgNB\nedFUbugHOICzepuTp0t5bPM/bpHDyadQBr99v1ypaphvBYhnlws8U0IQCR0zHB+I5OSXtB09fU3+\nOo2qsMIkyqv8UrujOj/WIbKoMM6niBrFpC298vFed5q8pR2NFaYI0cIvMIUIEv/AvKFFgGa/7ti+\n9Uzo0J8kz5CwR1xQWm5OYJ6Iec9o5sHXfe7b");
        //System.out.println(s);
        //ASEUtil.AESDncode("pjjkj", "yrRdCcH3rNNYwWvmv+dFku4bl4XdIGgS4bYZMf8QZ/iT+gU8DWsySYxx5UBmfYv7OxrjyY+gJBa4\nf/TzIUVdx9ERn6KM2amMcWZjX9L4XqUu4aSp9dZbTxICE6kb9vRT8pi+9V89DIzjoZ3nX3Cv0UrP\nXwimNtYT9uJheytjjoJnEHjCwRk+7dYisiYKEHLuPHvdkZrBnm0aX+FYfuepO8+jomk628b/+zp/\npwgOIqiAv5dXq8JeIiuzfqKEUFJ7QJumC7h/n830J9B53bc/b6awa14Fszb5389YVrZ0wPlr1jzi\nfs0NPQ1SF339WC8FbJawfN35vGfuBDnhwsSvNVIIpFHu3SIZcGKkUMjYul1eu4t7Icksqb1QJCJc\nyvE+eccfzUYe1CmV3flBHGAIyQAwa3ZLCkreThq6gzvziLt1D/FSrYKIiAQZZFYiP5/xMMfFe114\ny2ytQYlzZFjITwgKb8v34tnTzxaYFMUnZJG1/3ANXaYQGPNTn1TPx2iaGxb00R0P7dcvuAh+Spr9\nfzRG3x1FcNmTGNAdEElUn4mPk7ZH5+rOoUGVoy7jGd+hs+3MQyu7n/PjMNHAAJRsGnsGe9Ofm0b+\n0fBHDY5r1VVEen87I89njfmbJGWxfaurjwvgGsSCkTKN9ZE7N7SO7soodMNiKwZ2l8QdMjCA7tiV\nZE3OelbW2GcvwQ9+PYLCgKipguDh8h/kw6ZcBlskqfPpmZ/0APYgN2BHKqc5WfHelW5bzALeEBP/\nvU+UkW6ogUPLX+86UjZT/4aVmnNzbi8Plp5ptEIEYo0B3R2q17VJavCuZobMtPaQFqVBSNLEXN9o\ns41AcENofhU3/1G+kHrwQNHydSioJsQkJ+HfTZVuA8ZiV2F91bv8oVHPxACDtcfHZqYeXXKSUi3I\nP3z6RwSHJgZo+XzoLwf7QmM5xPmWtA2DcIAtn8gly+b/5cs8NRc+DcnTkDuMrzpzv0G89KtKFX+N\nK/wxGggProoKWVT/pLgrATrjONZx0p9K+4ID346Z6b6+NwFqkCyZa7iSaHFABNJ3TwHsNen8Duox\n72ou5B77wL5F42eglh2+TCEK");
        Class<ApiService> aClass = ApiService.class;
        Method[] methods = aClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Type callResponseType = getCallResponseType(methods[i].getGenericReturnType());
            if (callResponseType.getTypeName().contains("Result<")) {
                Method[] methods1 = callResponseType.getClass().getMethods();
                for (int j = 0; j < methods1.length; j++) {
                    System.out.println(methods1[i].getGenericReturnType().getTypeName());
                }
                return;
            }
            //System.out.println(callResponseType.getTypeName());
        }
        Thread.sleep(2000);
    }

    /**
     * 获取泛型 0
     *
     * @param index
     * @param type
     * @return
     */
    Type getParameterUpperBound(int index, ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        if (index < 0 || index >= types.length) {
            throw new IllegalArgumentException(
                    "Index " + index + " not in range [0," + types.length + ") for " + type);
        }
        Type paramType = types[index];
        if (paramType instanceof WildcardType) {
            return ((WildcardType) paramType).getUpperBounds()[0];
        }
        return paramType;
    }

    /**
     * 检测是否为泛型
     *
     * @param returnType
     * @return
     */
    Type getCallResponseType(Type returnType) {
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        return getParameterUpperBound(0, (ParameterizedType) returnType);
    }
}