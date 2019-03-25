package com.fable.demo.bussiness.service.fileService;

import com.fable.demo.bussiness.mapper.fileMapper.IFileMapper;
import com.fable.demo.common.pojo.FileList;
import com.fable.enclosure.bussiness.entity.DataTableResponse;
import com.fable.enclosure.bussiness.entity.PageRequest;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.service.impl.BaseServiceImpl;
import com.fable.enclosure.bussiness.util.ResultKit;
import com.fable.enclosure.bussiness.util.Tool;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/1/23
 * Time :11:12
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
@Service
public class FileServiceImpl extends BaseServiceImpl implements IFileService {

    final private String ideaContainer = File.separator + "target";
    final private String normalContainer = File.separator + "webapps";
    private String fileFolder;


    @Autowired
    IFileMapper mapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SolrClient solrClient;

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 文件存放位置，根据项目需要
     *
     * @return 文件父级文件夹
     */
    @Override

    public String getFileFolder(HttpServletRequest request) {
        if(StringUtils.isEmpty(fileFolder)){
            String uploadPath = request.getServletContext().getRealPath(
                    File.separator);
            if (uploadPath.contains(normalContainer)) {
                uploadPath = uploadPath.substring(0, uploadPath.indexOf(normalContainer));
                uploadPath = uploadPath.substring(0, uploadPath.lastIndexOf(File.separator));
            } else if (uploadPath.contains(ideaContainer)) {
                uploadPath = uploadPath.substring(0, uploadPath.indexOf(ideaContainer));
                uploadPath = uploadPath.substring(0, uploadPath.lastIndexOf(File.separator));
            } else {
                uploadPath = System.getProperty("user.dir");
            }
            fileFolder=uploadPath+ File.separator + "uploadFile";
        }
        return fileFolder;
    }

    /**
     * 删除文件，根据项目需要，操作进行数据库操作
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse deleteFile(Map<String, String> param) {
        File file = new File(fileFolder, param.get("fileUrl"));
        if (file.exists()) {
            if (file.delete()) {
                mapper.deleteFile(param);
                return ResultKit.success();
            }
            return ResultKit.fail("删除文件失败");
        }
        return ResultKit.fail("文件不存在");

    }

    @Override
    public BaseResponse getFileList(PageRequest<Map<String, String>> param) {
        Page<Map<String, Object>> result = PageHelper.startPage(param.getPageNo(), param.getPageSize());
        mapper.getFileList(param.getParam());
        return ResultKit.wrap(result);
    }

    @Override
    public BaseResponse addFile(Map<String, Object> param) {
        param.put("id", Tool.newGuid());
        param.put("createTime", new Date());
        return ResultKit.serviceResponse(mapper.addFile(param));
    }

    private  Map<String, Object> params = new HashMap<>();
    public synchronized void  addFileCrazy(String fileName,String fileUrl) {
        params.put("id", Tool.newGuid());
        params.put("fileName",fileName);
        params.put("fileUrl",fileUrl);
        mapper.addFile(params);
    }

    @Override
    public BaseResponse showPic() {
        return ResultKit.serviceResponse(mapper.showPic());
    }

    @Override
    /**
     * 组合条件查询
     *         // 创建组合条件串
     *         StringBuilder params = new StringBuilder("productType:" + productType);
     *
     *         // 组合商品颜色条件
     *         if (color != null) {
     *             params.append(" AND color:" + color);
     *         }
     *
     *         // 组合价格区间条件
     *         if (minPrice.intValue() != 0 || maxPrice != 0) {
     *             params.append(" AND spPrice:[" + minPrice + " TO "
     *                     + maxPrice + "]");
     *         }
     *
     *         solrQuery.setQuery(params.toString());
     */
    public BaseResponse solrService(PageRequest<Map<String, String>> param) {
        /**
         test_core是solr的一个core名称
         */
//        HttpSolrClient solrClient=new HttpSolrClient.Builder("http://192.168.20.11:9090/solr/").build();
        SolrQuery query = new SolrQuery();
        query.setSort("createTime", SolrQuery.ORDER.asc); //设置排序参数及排序规则
        query.setStart((param.getPageNo() - 1) * param.getPageSize());//起始页，这里一定要注意，不能直接把pageNo赋值给start,start表示从第一个数据开始，第一条从0开始。
        query.setRows(param.getPageSize());//每页显示数量
        StringBuilder buffer = new StringBuilder();
        Map<String, String> params = param.getParam();
        Iterator<String> it=params.keySet().iterator();
        //多个条件需要拼接成类似：     fileName:*1.txt* and fileUrl:*https://*
        while (it.hasNext()){
            String key = it.next();
            buffer.append(key + ":*" + params.get(key) + "* and ");//如果你的fbfmc字段在solrHome/fbf/conf/manage-schema文件中定义的类型是text_ik，即已经分词了，那么这里可以这么写,如果你定义的是string类型，即没有分词，那这句话的append中的内容需要写成这样buffer.append("fbfmc:*"+fbfmc+"*"),这是solr的查询规则，没有分词最好是加上模糊查询符号"*"
        }
        String bufferStr = buffer.toString();
        if(bufferStr.endsWith(" and ")){
            bufferStr = bufferStr.substring(0,bufferStr.lastIndexOf(" and "));
        }
        if(params.isEmpty()){
            query.set("q", "*:*"); //没有传入参数则全部查询
        }
        else{
            query.set("q", bufferStr);
        }
        QueryResponse rsp;
        List<FileList> fbfList = new ArrayList<>();
        SolrDocumentList results=new SolrDocumentList();
        try {
            rsp = solrClient.query(query);
            results = rsp.getResults();
            fbfList = rsp.getBeans(FileList.class);//该方法将返回结果转换为对象，很方便。
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataTableResponse<FileList> response = new DataTableResponse<>();
        response.setData(fbfList);
        response.setRecordsTotal(Integer.valueOf(String.valueOf(results.getNumFound())));

        return response;
//        SolrDocumentList results = rsp.getResults();
//        System.out.println(results.getNumFound());//查询总条数，该总条数是符合该条件下的总条数，并不是pageSize的数量。
    }

    @Override
    public BaseResponse search(PageRequest<Map<String, String>> param) throws IOException {
        String condition;
        if(param.getParam().isEmpty()){
            condition="q=*:*"; //没有传入参数则全部查询
        }
        else{
            condition="q=fileName:*"+param.getParam().get("fileName")+"*";
        }
        condition = condition + "&start=" + (param.getPageNo() - 1) * param.getPageSize()+"&rows="+param.getPageSize();
        String url = "http://192.168.20.197:8983/solr/test_core/select?"+condition;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
        logger.error("查询返回值：{}",responseEntity.getBody());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode node=mapper.readTree(responseEntity.getBody());
        JsonNode node1=node.get("response");
        JsonNode node2 = node1.get("numFound");
        JsonNode nodes=node1.get("docs");
        JavaType type=mapper.getTypeFactory().constructParametricType(List.class,FileList.class);
        List<FileList> list=mapper.readValue(nodes.toString(),type);
        DataTableResponse<FileList> response = new DataTableResponse<>();
        response.setData(list);
        response.setRecordsTotal(Integer.valueOf(node2.toString()));
        return response;
    }

//    @Scheduled(cron = "0/5 * * * * *")
    public void updateSolr(){
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("command", "delta-import");
        postParameters.add("verbose", "false");
        postParameters.add("clean", "false");
        postParameters.add("commit", "true");
        postParameters.add("core", "test_core");
        postParameters.add("name", "dataimport");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        String url = "http://192.168.20.197:8983/solr/test_core/dataimport?indent=on&wt=json";
        String responseMessage = restTemplate.postForObject(url, r, String.class);
        logger.error("更新solr索引：返回值：{}",responseMessage);
    }

}
