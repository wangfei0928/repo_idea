package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryTypeList() {

        //1. 先去Redis查询缓存首页的数据
        String shopTypeListJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_TYPE_KEY);

        //2. 如果不为空，则直接返回
        if (StrUtil.isNotBlank(shopTypeListJson)){
            return Result.ok(JSONUtil.toList(shopTypeListJson,ShopType.class));
        }

        //3. 如果为空，去查询数据
        List<ShopType> shopTypeList = this.query().orderByDesc("sort").list();

        //4. 如果查询结果为空，则直接返回错误信息
        if (shopTypeList == null || shopTypeList.size() == 0){
            return Result.fail("商品类型查询失败");
        }

        //5. 如果不为空，将查询到的结果缓存到redis中
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_TYPE_KEY,JSONUtil.toJsonStr(shopTypeList));
        return Result.ok(shopTypeList);
    }
}
