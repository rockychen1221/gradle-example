package com.littlefox.example.controller;

import com.littlefox.example.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/user")
@Api(value = "UserController",tags={"用户数据"})
public class UserController {

    @Resource
    private UserService userService;
//
//    /**
//     * 初始化数据推送到ElasticSearch
//     * @return
//     */
//    @ApiOperation(value="推送初始数据", notes="初始化数据,推送数据到ElasticSearch")
//    @RequestMapping(value = "/pushData", method = RequestMethod.GET)
//    public ResponseEntity pushData(){
//        if (CollectionUtils.isEmpty(list)) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//        factWeatherService.saveAll(list);
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    /**
//     * 根据地域查询
//     * @param area
//     * @return
//     */
//    @ApiOperation(value="根据地域查询前十条", notes="根据地域查询前十条详细信息")
//    @ApiImplicitParam(name = "area", value = "地域", required = true, dataType = "String", paramType = "path")
//    @RequestMapping(value = "findByArea/{area}", method = RequestMethod.GET)
//    public ResponseEntity findByArea (@PathVariable(value = "area") String area){
//        if (area.isEmpty()){
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//        Page<FactWeather> page = factWeatherService.pageQuery(0, 10, area);
//        return new ResponseEntity(page , HttpStatus.OK);
//    }
//
//    /**
//     * 根据地域查询
//     * @param area
//     * @return
//     */
//    @ApiOperation(value="根据地域和天气查询", notes="根据地域和天气查询详细信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "area", value = "地域", required = true),
//            @ApiImplicitParam(name = "weather", value = "天气", required = true)
//    })
//    @RequestMapping(value = "findByAreaAndWeather", method = RequestMethod.GET)
//    public ResponseEntity findByAreaAndWeather (@RequestParam(value = "area") String area, @RequestParam(value = "weather") String weather){
//        if (area.isEmpty()){
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity(factWeatherService.findByAreaAndWeather(area,weather), HttpStatus.OK);
//    }
//
//    /**
//     * 获取所有数据
//     * @return
//     */
//    @ApiOperation(value = "获取天气总数量", notes = "获取所有天气总数")
//    @RequestMapping(value = "/getAllCount", method = RequestMethod.GET)
//    public ResponseEntity getAllCount() {
//        return new ResponseEntity(factWeatherService.count(), HttpStatus.OK);
//    }
//
//    /**
//     * 全字段查询前十条
//     * @param str
//     * @return
//     */
//    @ApiOperation(value="全字段查询前十条", notes="查询前十条详细信息")
//    @ApiImplicitParam(name = "str", value = "值", required = true, dataType = "String", paramType = "path")
//    @RequestMapping(value = "pageQueryAll/{str}", method = RequestMethod.GET)
//    public ResponseEntity pageQueryAll (@PathVariable(value = "str") String str){
//        if (str.isEmpty()){
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
//        Page<FactWeather> page = factWeatherService.pageQueryAll(0, 10, str);
//        return new ResponseEntity(page , HttpStatus.OK);
//    }

}
