package cn.su.article.service;

import cn.su.article.dao.MedicalDao;
import cn.su.article.po.Medical;
import com.mongodb.BasicDBObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MedicalDaoImpl implements MedicalDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Medical findById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query,Medical.class);
    }

    @Override
    public List<Medical> findAll() {
        return mongoTemplate.findAll(Medical.class);
    }

    @Override
    public void saveMedical(Medical medical) {
        mongoTemplate.save(medical);
    }

    @Override
    public void removeMedical(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Medical.class);
    }

    @Override
    public void updateMedical(Medical medical) {
        Query query = new Query(Criteria.where("_id").is(medical.getId()));

        Update update = new Update();
        update.set("num",medical.getNum());
        update.set("price",medical.getPrice());

        mongoTemplate.updateFirst(query,update,Medical.class);
    }

    @Override
    public List<Medical> findByPage(Medical medical, int skip, int limit, Sort sort) {
        Query query =new Query();
        if (medical.getCategory()!= null){
            query.addCriteria(Criteria.where("category").is(medical.getCategory()));
        }
        if (medical.getNum() != null){
            query.addCriteria(Criteria.where("num").is(medical.getNum()));
        }
        query.skip(skip).limit(limit).with(sort);
        return mongoTemplate.find(query,Medical.class);
    }

    @Override
    public AggregationResults<BasicDBObject> findByAggregation(Aggregation agg){
        return mongoTemplate.aggregate(agg,"findByAggregate",BasicDBObject.class);
    }


}
