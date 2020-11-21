package Goods.controller;

import Goods.Threads.MyRunnable1;
import Goods.Threads.MyRunnable2;
import Goods.json.DataChanges;
import Goods.json.GoodChanges;
import Goods.json.Statistic;
import Goods.model.Goods;
import Goods.model.Price;
import Goods.repositories.PriceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import Goods.repositories.GoodsRepo;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
public class Main   {
    @Autowired
    volatile GoodsRepo goodsRepo;
    @Autowired
    volatile PriceRepo pricesRepo;

    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Value("${goods.location}")
    private String path;

    @GetMapping("/products")
    public void greeting(@RequestParam String date) throws IOException, ParseException {
        checkFolder();
        List<Price> goods = pricesRepo.findAllByDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        if(goods.size()!=0){
            toJSON(goods);

        }else{
            LOGGER.log(Level.INFO,"Отмена создания пустого файла");

        }

    }

    @GetMapping("/products/statistic")
    public  void getStats() throws IOException, ParseException, InterruptedException {
        MyRunnable1 runnable = new MyRunnable1();
        MyRunnable2 runnable2 = new MyRunnable2();
        MyRunnable2 runnable3 = new MyRunnable2();
        Thread t1 = new Thread(runnable) {
            @Override
            public void run() {
                runnable.setCount(goodsRepo.count());
            }

        };
        Thread t2 = new Thread(runnable2) {
            @Override
            public void run() {
                runnable2.setLst(pricesRepo.getGoodChanges());
            }

        };
        Thread t3 = new Thread(runnable3) {
            @Override
            public void run() {
                runnable3.setLst(pricesRepo.getDataChanges());
            }

        };
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        Statistic statistic = new Statistic(runnable.getCount(),toGoodChangesList(runnable2.getLst()),toDataChangesList(runnable3.getLst()));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.writeValue(new File("statistic.txt"),statistic);


    }


    public static void toJSON(List<Price> price) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(price.get(0).getDate().toString()+".txt"), price);
        LOGGER.log(Level.INFO,"Создан файл "+ price.get(0).getDate().toString()+".txt");
    }

    public static List<GoodChanges> toGoodChangesList(List<Object[]> list) throws ParseException {
        List<GoodChanges> goodChanges = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            GoodChanges goodChanges1 = new GoodChanges(list.get(i)[0].toString(),Long.valueOf(list.get(i)[1].toString()));
            goodChanges.add(goodChanges1);
        }
        return goodChanges;
    }

    public static List<DataChanges> toDataChangesList(List<Object[]> list) throws ParseException {
        List<DataChanges> dataChanges = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            DataChanges dataChanges1 = new DataChanges(new SimpleDateFormat("yyyy-MM-dd").parse(list.get(i)[0].toString()),Long.valueOf(list.get(i)[1].toString()));
            dataChanges.add(dataChanges1);
        }
        return dataChanges;
    }



    public void readFromFile(String path) throws ParseException {
        if(new File(path).exists()) {
            List<List<String>> records = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                LOGGER.log(Level.INFO,"Началось считывание из файла");
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");
                    records.add(Arrays.asList(values));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int numbRows = records.size();
            LOGGER.log(Level.INFO,"Считано " + numbRows + " строк");
            for (int i = 0; i < records.size(); i++) {
                if (goodsRepo.findAllById(Integer.parseInt(records.get(i).get(0))).size() == 0) {
                    Goods good = new Goods(Integer.parseInt(records.get(i).get(0)), records.get(i).get(1));
                    goodsRepo.save(good);
                    if (pricesRepo.findAllById(Integer.parseInt(records.get(i).get(2))).size() == 0) {
                        Price price = new Price(Integer.parseInt(records.get(i).get(2)), Double.valueOf(records.get(i).get(3)), new SimpleDateFormat("yyyy-MM-dd").parse(records.get(i).get(4)), good);
                        pricesRepo.save(price);
                    } else {
                        numbRows--;
                    }
                } else {
                    numbRows--;
                }
            }
            LOGGER.log(Level.INFO,"Добавлено " + numbRows + " строк в БД");
        }

    }

    public void checkFolder() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!new File(path).exists()) {
                    try {
                        System.out.println("Ждёт");
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    readFromFile(path);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("Выполнил");

            }
        });
        t1.start();
    }

}
