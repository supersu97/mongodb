package cn.itcast.article.service;

import cn.su.article.dao.MedicalDao;
import cn.su.article.po.Medical;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MedicalServiceTest {

    @Autowired
    private MedicalDao medicalDao;

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void findById(){
        Medical medical = medicalDao.findById("1");
        System.out.println(medical);
    }

    @Test
    public void findAll(){
        System.out.println(medicalDao.findAll());
    }

    @Test
    public void saveMedical(){
        Medical medical = new Medical();
        medical.setId("6");
        medical.setName("六味地黄丸");
        medical.setCategory("中成药");
        medical.setNum(10);
        medical.setPrice(9.5);
        medicalDao.saveMedical(medical);
    }

    @Test
    public void removeMedical(){
        medicalDao.removeMedical("6");
    }

    @Test
    public void updateMedical(){
        Medical medical = new Medical();
        medical.setId("1");
        medical.setNum(100);
        medical.setPrice(23.5);
        medicalDao.updateMedical(medical);
    }

    @Test
    public void findByPage(){
        Sort sort = Sort.by(Sort.Order.desc("price"));
        Medical medical = new Medical();
        //medical.setCategory("西药");
        System.out.println(medicalDao.findByPage(medical,1,3,sort));
    }

    @Test
    public void aggregate(){
        Aggregation agg = newAggregation(
               //count().as("count")
                //group("category").count().as("count"),
            lookup("medical","med_id","_id","medicalDetail"),
            project("_id","med_id","num","medicalDetail"),
            skip(0L),limit(5),sort(Sort.by(Sort.Order.desc("medicalDetail.price")))
        );
        AggregationResults<BasicDBObject> results = mongoTemplate.aggregate(agg,"medOrder", BasicDBObject.class);
        List<BasicDBObject> a = results.getMappedResults();
        System.out.println(JSONObject.toJSONString(a));
    }



}
