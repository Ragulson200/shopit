package com.manway.customermodule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface DataBaseConverter{
    public static   String convertKeyTOTable(@NotNull HashMap<String, Object> map, @NotNull String tableName) {
        ArrayList<String> l = new ArrayList<>(map.keySet());
        StringBuilder i = new StringBuilder("create table if not EXISTS "+tableName+" (id integer primary key autoincrement");
        for (String s : l) {
            if (map.get(s) instanceof Integer) {
                i.append(",").append(s).append(" integer");
            } else if (map.get(s) instanceof String) {
                i.append(",").append(s).append(" text");
            } else if (map.get(s) instanceof Boolean) {
                i.append(",").append(s).append(" NUMERIC");
            } else if (map.get(s) instanceof Float){
                i.append(",").append(s).append(" REAL");
            } else if (map.get(s) instanceof Double) {
                i.append(",").append(s).append(" REAL");
            } else {
                i.append(",").append(s).append(" blob");
            }
        }
        i.append(')');
        return i.toString();
    }



    public static   String convertKeyTOTableString(@NotNull HashMap<String,String> map, @NotNull String tableName) {
        ArrayList<String> l = new ArrayList<>(map.keySet());
        StringBuilder i = new StringBuilder("create table if not EXISTS "+tableName+" (id integer primary key autoincrement");
        for (String s : l) {
                i.append(",").append(s).append(" text");
        }
        i.append(')');
        return i.toString();
    }


    public class HashData{

        public static boolean isHashData(String s){
            Pattern p=Pattern.compile("((\\|map\\|)[\\w\\W]+(\\|map\\|))"+"|((\\|v\\|)[\\w\\W][^|]+(\\|v\\|))");
            Matcher matcher=p.matcher(s);
            Map<String,String> map=new HashMap<>();
            return matcher.find();
        }

        private HashMap<String,String> map;
        private String str;
        public HashData(Map<String,String> map){
            this.map= (HashMap<String, String>) map;
            str=convertMapToData((HashMap<String, String>) map);
        }

        public HashData(String s){
            this.map=convertDataTOMap(s).get(0);
            str=s;
        }

        public String toString() {
            return str;
        }

        public HashMap<String,String> toMap(){
            return map;
        }

        public static String convertMapToData(HashMap<String,String> map){
            StringBuffer buffer=new StringBuffer("|map|");
            ArrayList<String> list=new ArrayList<>(map.keySet());
            for(String s:list){
                buffer.append("|k|"+s+"|k|"+"|v|"+map.get(s)+"|v|");
            }
            buffer.append("|map|");
            return buffer.toString();
        }

        public static ArrayList<HashMap<String,String>> convertDataTOMap(String value){
            Pattern p=Pattern.compile("\\|map\\|");
            Matcher matcher=p.matcher(value);
            ArrayList<HashMap<String,String>> data=new ArrayList<>();
            HashMap<String,String> map=new HashMap<>();
            int a=0,b=0,c=0,d=0;
            while (matcher.find()){
                map=new HashMap<>();
                b = matcher.start();
                ArrayList<String> list=new ArrayList<>();
                try {
                    if (!value.substring(a, b).equals("")) {
                        // System.out.println(value.substring(a+5, b)+" "+a+":"+b);
                        Pattern p1 = Pattern.compile("(\\|k\\|)|(\\|v\\|)");
                        Matcher matcher1 = p1.matcher(value.substring(a + 5, b));
                        String temp = value.substring(a + 5, b);
                        c = 0;
                        d = 0;
                        while (matcher1.find()) {
                            d = matcher1.start();
                            if (!temp.isEmpty()) {
                                list.add(temp.substring(c, d));
                                c = matcher1.end();
                            }
                        }
                        for (int i = 1; i < list.size(); i += 4) {
                            map.put(list.get(i), list.get(i + 2));
                        }
                        a = matcher.end();
                    }
                    data.add(map);
                }catch (Exception e){}
            }
            return  data;
        }

    }

    public class ArrayData{

        public static boolean isArray(String s){
            Pattern p=Pattern.compile("((\\|a\\|)[\\w\\W]+(\\|a\\|))");
            Matcher matcher=p.matcher(s);
            return matcher.find();
        }

        public static boolean isArrayMap(String s){
            Pattern p=Pattern.compile("((\\|a\\|)[\\w\\W]+(\\|a\\|))");
            Matcher matcher=p.matcher(s);
            Boolean bol=false;
            if(matcher.find()){
                Pattern p1=Pattern.compile("((\\|map\\|)[\\w\\W]+(\\|map\\|))");
                Matcher matcher1=p1.matcher(s.substring(3,matcher.group().length()-3));
                bol=matcher1.find();
            }
            return bol;
        }


        private ArrayList<String> arrayList;
        private String strs;

        public ArrayData(ArrayList<String> arrayList){
            this.arrayList=arrayList;
            this.strs=convertArrayToData(arrayList);
        }

        public ArrayData(String s){
            this.arrayList=convertDataToArray(s);
            this.strs=s;
        }

        public String toString(){
            return strs;
        }

        public ArrayList<String> toArray(){
            return arrayList;
        }


        public static String convertArrayToData(ArrayList<String > arrayList){
            StringBuffer stringBuffer=new StringBuffer("|a|");
            if(arrayList.equals(null)||arrayList.size()==0) {
                stringBuffer =new StringBuffer("");
            }
            else {
                stringBuffer.append(arrayList.get(0));
                for(int i=1;i<arrayList.size();i++){
                    stringBuffer.append('|'+arrayList.get(i));
                }
                stringBuffer.append("|a|");
            }
            return stringBuffer.toString();
        }

        public static ArrayList<String> convertDataToArray(String s){
            Pattern p=Pattern.compile("\\[?((\\|a\\|)[\\w\\W]+(\\|a\\|))\\]?");
            Matcher matcher=p.matcher(s);
            ArrayList<String> list = new ArrayList<>();
            while (matcher.find()) {
                String[] array = matcher.group().substring(3,matcher.group().length()-3).split("\\|");
                for (String str : array) {
                    if(!str.equals("")) {
                        list.add(str);
                    }
                }
            }
            return list;
        }
    }

    public static byte[] convertImageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Uri convertBitmapToUri(String fileName, Bitmap bitmap){
        try {
            File file=File.createTempFile(fileName,".jpeg");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            FileOutputStream fileOutputStream= new FileOutputStream(file);
            fileOutputStream.write(byteArray);
            fileOutputStream.flush();
            return Uri.fromFile(file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}



