package com.atguigu.srb.core;

import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.sun.xml.bind.v2.model.core.ID;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import sun.security.provider.MD5;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class test2 {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private DictMapper dictMapper;

    @Test
    public void test1(){
        Dict dict = dictMapper.selectById(1);
        System.out.println(dict);
        redisTemplate.opsForValue().set("dict",dict,5, TimeUnit.HOURS);
    }

    private String sendSms(String accName,String accPwd,String mobies,String content){
        StringBuffer sb = new StringBuffer("http://sdk.lx198.com/sdk/send?");
        try {
            sb.append("&accName="+accName);
            sb.append("&accPwd="+ md5(accPwd));
            sb.append("&aimcodes="+mobies);
            sb.append("&content="+ URLEncoder.encode(content,"UTF-8"));
            sb.append("&dataType=string");
            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            return in.readLine();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //    MD5??????
    public static String md5(String key){
        if (StringUtils.isEmpty(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }


    @Test
    public void test2(){
        String s = sendSms("15675436505", "chen20031014", "15675436505", "???????????????", "2010-01-01 08:00:00");
        System.out.println(s);

    }



    /**
     * ????????????
     * @param accName ?????????
     * @param accPwd ??????
     * @param
     * @param content ??????????????????
     * @param schTime ????????????????????????2010-01-01 08:00:00
     * @return ???????????????????????? ok:??????id ?????? ????????????
     */
    public  static String sendSms(String accName,String accPwd,String mobies,String content,String schTime){
        StringBuffer sb = new StringBuffer("https://www.lx598.com/sdk/send?");
        try {
            sb.append("&accName="+accName);
            sb.append("&accPwd="+md5(accPwd));
            sb.append("&aimcodes="+mobies);
            sb.append("&schTime="+URLEncoder.encode(schTime,"UTF-8")); //?????????????????????encode??????
            sb.append("&content="+URLEncoder.encode(content,"UTF-8")); //?????????encode??????
            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * ????????????
     * @param accName ?????????
     * @param accPwd ????????????
     * @return ???????????????????????? ok:??????id ?????? ????????????
     */
    public  static String qryBalance(String accName,String accPwd){
        StringBuffer sb = new StringBuffer("https://www.lx598.com/sdk/qryBalance?");
        try {
            sb.append("&accName="+accName);
            sb.append("&accPwd="+md5(accPwd));
            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            return in.readLine();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ??????????????????
     * @param accName ?????????
     * @param accPwd ????????????
     * @return ??????????????????????????? ??????????????????id,?????????,??????;??????id,?????????,??????   ??????1???????????????0????????????
     */
    public  static String qryReport(String accName,String accPwd){
        StringBuffer sb = new StringBuffer("https://www.lx598.com/sdk/qryReport?");
        try {
            sb.append("&accName="+accName);
            sb.append("&accPwd="+md5(accPwd));
            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ??????????????????
     * @param accName ?????????
     * @param accPwd ??????
     * @return
     */
//    public  static String receiveSms(String accName,String accPwd){
//        StringBuffer sb = new StringBuffer("https://www.lx598.com/sdk/receiveSms?");
//        try {
//            String seed=new SimpleDateFormat(dateFormatStr).format(new Date());
//            sb.append("&accName="+accName);
//            sb.append("&accPwd="+md5(accPwd));
//            URL url = new URL(sb.toString());
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//            return in.readLine();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
