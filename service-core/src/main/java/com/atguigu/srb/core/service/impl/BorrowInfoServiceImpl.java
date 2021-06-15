package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.core.enums.BorrowInfoStatusEnum;
import com.atguigu.srb.core.enums.BorrowerStatusEnum;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.mapper.IntegralGradeMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.mapper.BorrowInfoMapper;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.service.BorrowInfoService;
import com.atguigu.srb.core.service.BorrowerService;
import com.atguigu.srb.core.service.DictService;
import com.atguigu.srb.core.service.LendService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private IntegralGradeMapper integralGradeMapper;

    @Autowired
    private DictService dictService;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Autowired
    private BorrowerService borrowerService;

    @Autowired
    private LendService lendService;

    @Override
    public BigDecimal getBorrowAmount(Long userId) {
//        获取用户积分
        UserInfo userInfo = userInfoMapper.selectById(userId);
//        校验用户是否存在
        Assert.notNull(userInfo, ResultEnum.LOGIN_MOBILE_ERROR);
        Integer integral = userInfo.getIntegral();

//        根据积分查询额度
        QueryWrapper<IntegralGrade> wrapper = new QueryWrapper<>();
        wrapper.le("integral_start",integral).ge("integral_end",integral);

        IntegralGrade integralGrade = integralGradeMapper.selectOne(wrapper);
        Assert.notNull(integralGrade,ResultEnum.BORROW_AMOUNT_NULL_ERROR);

        return integralGrade.getBorrowAmount();
    }

    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo, Long userId) {

//        判断用户绑定的状态
        UserInfo userInfo = userInfoMapper.selectById(userId);

//        判断借款人额度申请状态
        Assert.isTrue(userInfo.getBorrowAuthStatus().intValue() ==
           BorrowerStatusEnum.AUTH_OK.getStatus().intValue(), ResultEnum.USER_NO_AMOUNT_ERROR);

//        判断借款额度是否足够
        BigDecimal borrowAmount = this.getBorrowAmount(userId);
        Assert.isTrue(
                borrowInfo.getAmount().doubleValue() <= borrowAmount.doubleValue(),
                ResultEnum.USER_AMOUNT_LESS_ERROR);

//      存储数据
        borrowInfo.setUserId(userId);
//      百分比转成小数
        borrowInfo.setBorrowYearRate( borrowInfo.getBorrowYearRate().divide(new BigDecimal(100)));
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        baseMapper.insert(borrowInfo);

    }

    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<BorrowInfo> borrowInfoQueryWrapper = new QueryWrapper<>();
        borrowInfoQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowInfoQueryWrapper);
        if(objects.size() == 0){
            //借款人尚未提交信息
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }
        Integer status = (Integer)objects.get(0);
        return status;
    }

    @Override
    public List<BorrowInfo> selectList() {
        List<BorrowInfo> borrowInfoList = baseMapper.selectBorrowInfoList();
        for (BorrowInfo borrowInfo : borrowInfoList) {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
            String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getReturnMethod());
            String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());  //根据状态的code返回状态的message
            borrowInfo.getParam().put("returnMethod",returnMethod);
            borrowInfo.getParam().put("moneyUse",moneyUse);
            borrowInfo.getParam().put("status", status);
        }
        return borrowInfoList;
    }

    @Override
    public Map<String, Object> getBorrowInfoDetail(Long id) {
        Map<String, Object> map = new HashMap<>();
//        查询借款对象 BorrowInfo
        BorrowInfo borrowInfo = baseMapper.selectById(id);
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
        String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getReturnMethod());
        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());  //根据状态的code返回状态的message
        borrowInfo.getParam().put("returnMethod",returnMethod);
        borrowInfo.getParam().put("moneyUse",moneyUse);
        borrowInfo.getParam().put("status", status);

//        查询借款人对象 Borrower
        QueryWrapper<Borrower> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",borrowInfo.getUserId());
        Borrower borrower = borrowerMapper.selectOne(wrapper);
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());

//        组装集合结果
        map.put("borrowInfo",borrowInfo);
        map.put("borrower",borrowerDetailVO);
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {

//        修改借款审核的状态 borrow-info
        Long id = borrowInfoApprovalVO.getId(); //borrow ID
        BorrowInfo borrowInfo = baseMapper.selectById(id);
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus()); //设置状态
        baseMapper.updateById(borrowInfo);

//      审核通过则创建标的
        if (borrowInfoApprovalVO.getStatus().intValue() == BorrowInfoStatusEnum.CHECK_OK.getStatus().intValue()) {
            //创建标的
            lendService.createLend(borrowInfoApprovalVO,borrowInfo);
        }

    }
}
