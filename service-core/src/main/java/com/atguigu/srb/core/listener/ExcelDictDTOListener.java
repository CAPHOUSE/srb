package com.atguigu.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Slf4j
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {



    private DictMapper dictMapper;

    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    private List<ExcelDictDTO> list = new ArrayList<>();

//    每隔五条数据执行SQL
    private static final int BATCH_COUNT = 5;

    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext analysisContext) {
        log.info("解析一条记录: {}",data);
//        将数据存入数据列表
        list.add(data);

        if (list.size() >= BATCH_COUNT){
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        log.info("所有数据解析完成");
    }

    private void saveData(){
        log.info("{} 条数据被存储到数据库",list.size());
//        调用mapper的save方法
        dictMapper.insertBatch(list);
        log.info("{} 条数据被存储到数据库成功",list.size());

    }
}
