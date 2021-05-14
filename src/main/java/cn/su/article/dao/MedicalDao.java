package cn.su.article.dao;


import cn.su.article.po.Medical;
import com.mongodb.BasicDBObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MedicalDao {
    Medical findById(String id);

    List<Medical> findAll();

    void saveMedical(Medical medical);

    void removeMedical(String id);

    void updateMedical(Medical medical);

    List<Medical> findByPage(Medical medical, int skip, int limit, Sort sort);

    AggregationResults<BasicDBObject> findByAggregation(Aggregation agg);
}
