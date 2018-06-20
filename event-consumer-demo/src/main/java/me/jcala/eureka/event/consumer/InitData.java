package me.jcala.eureka.event.consumer;

import me.jcala.eureka.event.consumer.domain.Repertory;
import me.jcala.eureka.event.consumer.mapper.RepertoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@Component
public class InitData {

    @Autowired
    private RepertoryMapper repertoryMapper;

    @PostConstruct
    public void initData() {
        int count = repertoryMapper.selectCount(new Repertory("apple"));
        if (count < 1) {
            Repertory repertory = new Repertory();
            repertory.setNum(2334L);
            repertory.setItemType("apple");
            repertoryMapper.insert(repertory);
            repertory.setId(null);
            repertoryMapper.insert(repertory);
            repertory.setId(null);
            repertoryMapper.insert(repertory);
        }

        Repertory repertory11 = new Repertory();
        repertory11.setNum(2334L);
        Repertory select = repertoryMapper.selectOne(repertory11);
        System.out.println(select);
    }

}
