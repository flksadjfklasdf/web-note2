package com.web.note.service;

import com.web.note.dao.IpBanMapper;
import com.web.note.entity.IpBan;
import com.web.note.entity.IpBanExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class IpBanService {

    @Autowired
    IpBanMapper ipBanMapper;




    public void banIp(String ip, int minutes, Integer type) {
        IpBanExample example = new IpBanExample();
        example.createCriteria().andIpEqualTo(ip);  // 通过 IP 查找是否有封禁记录

        // 查询是否已存在封禁记录
        List<IpBan> existingBans = ipBanMapper.selectByExample(example);

        // 设置封禁的起始时间和结束时间
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, minutes);
        Date endTime = calendar.getTime();  // 计算结束时间

        if (!existingBans.isEmpty()) {
            IpBan existingBan = existingBans.get(0);  // 假设每个 IP 只有一个封禁记录

            if (type >= existingBan.getBanType()) {
                // 如果封禁类型相同或比已存在的高，更新封禁的起始日期、结束日期和类型
                existingBan.setStartTime(now);
                existingBan.setEndTime(endTime);
                existingBan.setBanType(type);
                ipBanMapper.updateByPrimaryKey(existingBan);
            }
            // 如果已存在的封禁类型更高，则保持不变
        } else {
            // 如果记录不存在，则新增封禁信息
            IpBan newBan = new IpBan();
            newBan.setIp(ip);
            newBan.setStartTime(now);
            newBan.setEndTime(endTime);
            newBan.setBanType(type);
            ipBanMapper.insert(newBan);
        }
    }
    public boolean isIpBanned(String ip) {
        // 创建查询条件
        IpBanExample example = new IpBanExample();
        IpBanExample.Criteria criteria = example.createCriteria();

        // 设置查询条件为：IP 地址等于给定的 IP，封禁类型大于 0，且结束时间晚于当前时间
        criteria.andIpEqualTo(ip);
        criteria.andBanTypeGreaterThan(0);
        criteria.andEndTimeGreaterThan(new Date());  // 当前时间

        // 执行查询
        List<IpBan> ipBans = ipBanMapper.selectByExample(example);

        // 如果查询结果非空，则表示该 IP 被封禁
        return !ipBans.isEmpty();
    }
    public void unbanIp(String ip, Integer type) {
        // 创建查询条件
        IpBanExample example = new IpBanExample();
        IpBanExample.Criteria criteria = example.createCriteria();

        // 设置查询条件为：IP 地址等于给定的 IP，封禁类型小于等于给定的类型
        criteria.andIpEqualTo(ip);
        criteria.andBanTypeLessThanOrEqualTo(type);

        // 执行查询
        List<IpBan> ipBans = ipBanMapper.selectByExample(example);

        // 遍历查询结果，删除符合条件的封禁记录
        for (IpBan ipBan : ipBans) {
            ipBanMapper.deleteByPrimaryKey(ipBan.getIp());
        }
    }



}
