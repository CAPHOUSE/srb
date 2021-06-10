package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.enums.BorrowerStatusEnum;
import com.atguigu.srb.core.mapper.BorrowerAttachMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.atguigu.srb.core.service.BorrowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BorrowerAttachMapper borrowerAttachMapper;

    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {

//       获取用户基本信息
        UserInfo userInfo = userInfoMapper.selectById(userId);

//        保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO,borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());  //认证中

        baseMapper.insert(borrower);

//        保存附件
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        for (BorrowerAttach borrowerAttach : borrowerAttachList) {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        }

//      更新userInfo中的借款人认证状态
        userInfo.setBindStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }
}
