package com.sf.kh.controller;
import java.util.Date;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.web.WebContext;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.kh.model.Article;
import com.sf.kh.model.ArticleType;
import com.sf.kh.model.User;
import com.sf.kh.service.IArticleService;
import com.sf.kh.service.IArticleTypeService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.ImageUtil;
import com.sf.kh.util.WebContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Auther: 01378178
 * @Date: 2018/6/27 15:35
 * @Description:
 */
@RestController
@RequestMapping(path="article")
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IArticleService articleService;

    @Resource
    private IArticleTypeService articleTypeService;

    @Value("${sharePath}")
    private String DEFAULT_FILE_PATH;

    private static final String B64_IMG_MARK = "image/";
    private static final String B64_MARK = "base64,";

    @GetMapping("getArticleType")
    public Result<List<ArticleType>> getAllArticleType(){
        List<ArticleType> types = articleTypeService.getAllArticleType();
        return Result.success(CollectionUtils.isEmpty(types) ? Lists.newArrayList(): types);
    }


    @PostMapping(path = "add")
    public Result<Map> addArticle(@RequestBody Article article) {

        Date d = new Date();
        User user = WebContextHolder.currentUser();
        article.setAuthorId(user==null? null : user.getId());
        article.setAuthorName(user==null? null : user.getUsername());
        article.setCreateBy(user==null? null : user.getId());
        article.setUpdateBy(user==null? null : user.getId());

        article.setUpdateTm(d);
        article.setCreateTm(d);
        article.setPublishStatus(Article.PublishStatusEnum.NOT_PUBLISH.getCode());

        preHandleContent(article);

        int i = articleService.addArticle(article);
        if(i==0){
            return Result.failure(ResultCode.SERVER_ERROR, "添加文章失败, 请重试");
        }
        return Result.success(ImmutableMap.of("id", article.getId()));
    }

    @PostMapping(path="publish")
    public Result<Map> publishArticle(@RequestBody Article article){

        User user = WebContextHolder.currentUser();
        Date d = new Date();

        if(article.getId() == null){
            article.setAuthorId(user==null? null : user.getId());
            article.setAuthorName(user==null? null : user.getUsername());
            article.setCreateBy(user==null? null : user.getId());
            article.setCreateTm(d);

        }

        article.setUpdateBy(user==null? null : user.getId());
        article.setPublishStatus(Article.PublishStatusEnum.PUBLISHED.getCode());
        article.setPublishTm(d);
        article.setUpdateTm(d);

        preHandleContent(article);

        int i = articleService.addArticle(article);
        if(i==0){
            return Result.failure(ResultCode.SERVER_ERROR, "添加文章失败, 请重试");
        }
        return Result.success(ImmutableMap.of("id", article.getId()));

    }


    /**
     *
     * @param params
     *  pageNum     页码
     *  pageSize    每页大小
     *  titleFuzzy  文章title关键字, 后匹配模式
     *  typeId      类别
     * @return
     */
    @GetMapping(path = "getArticle")
    public Result<Page<Article>> getArticle(PageRequestParams params){

        Map<String, Object> queryParams = params.getParams();

        if(queryParams.get(Constants.PAGE_NUM)==null || queryParams.get(Constants.PAGE_SIZE)==null){
            return Result.failure(ResultCode.BAD_REQUEST.getCode(), "分页条件不能为空");
        }

        List<Article> articles = articleService.list(queryParams);
        if(CollectionUtils.isEmpty(articles)){
            articles = Lists.newArrayList();
        }

        return Result.success(new Page<>(articles));
    }


    /**
     * 获取公告, 简单文本类型.
     * @param params
     * @return
     */
    @GetMapping(path="getNotice")
    public Result<Page<Article>> getNotice(PageRequestParams params){

        Map<String, Object> queryParams = params.getParams();
        if(queryParams.get("pageNum")==null || queryParams.get("pageSize")==null){
            return Result.failure(ResultCode.BAD_REQUEST.getCode(), "分页条件不能为空");
        }
        queryParams.put("typeId", 5);
        List<Article> articles = articleService.listWithContent(queryParams);
        return Result.success(new Page(CollectionUtils.isEmpty(articles) ? Lists.newArrayList() : articles));
    }

    @GetMapping(path="latestNotice")
    public Result<List<Article>> getLatestNotice(){

        Map<String, Object> params = Maps.newHashMap();
        params.put("pageNum","1");
        params.put("pageSize","10");
        params.put("typeId", 5);

        List<Article> articles = articleService.list(params);

        return Result.success(CollectionUtils.isEmpty(articles) ? Lists.newArrayList() : articles);
    }



    @GetMapping(path="getById")
    public Result<Article> getById(@RequestParam(value = "id", required = true) Long id){

        Article article = articleService.getById(id);

        if(article!=null) {
            String content = toBase64(article.getContent());
            article.setContent(content);
        }
        return Result.success(article);
    }




    @PostMapping(path = "update")
    public Result<Void> updateById(@RequestBody Article article){
        if(article==null || article.getId() == null){
            return Result.failure(ResultCode.BAD_REQUEST.getCode(), "参数或id不能为空");
        }

        Date d = new Date();
        User user = WebContextHolder.currentUser();
        article.setUpdateBy(user==null? null : user.getId());
        article.setUpdateTm(d);
        article.setPublishTm(d);

        preHandleContent(article);
        int i = articleService.updateArticleTypeById(article);

        if(i!=1){
            return Result.failure(ResultCode.SERVER_ERROR.getCode(), "更新失败, 请重试");
        }
        return Result.success();
    }


    @PostMapping(path="deleteById")
    public Result<Void> deleteById(@RequestBody Article article){

        if (article==null || CollectionUtils.isEmpty(article.getIds())) {
            return Result.failure(ResultCode.BAD_REQUEST.getCode(), "参数不能为空");
        }

        int n = articleService.deleteArticleBatch(article.getIds());
        if(n!=0) {
            return Result.success();
        }
        return Result.failure(ResultCode.SERVER_ERROR.getCode(), "删除失败，请重试");

    }


    public String convertForth(String str){
        if(StringUtils.isNotBlank(str)){
            str = str.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;","\"");
        }
        return str;
    }

    public String convertBack(String str){
        if(StringUtils.isNotBlank(str)){
            str = str.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\"","&quot;");
        }
        return str;
    }


    public void preHandleContent(Article article){

        String content = convertForth(article.getContent());

        Pattern p =  Pattern.compile("^data:image/(png|gif|jpg|jpeg|bmp|tif|psd|ICO);base64,.*");
        Document doc = Jsoup.parse(content,"utf-8");
        Elements imgs = doc.getElementsByTag("img");
        if(!imgs.isEmpty()) {
            for (Element img : imgs) {
                String src = img.attr("src");
                if (p.matcher(src).find()) {
                    String srcSuffix = src.substring((src.indexOf(B64_IMG_MARK) + (B64_IMG_MARK).length()),
                            (src.indexOf(B64_IMG_MARK) + (B64_IMG_MARK).length()) + 3);

                    String imgBase64 = src.substring((src.indexOf(B64_MARK) + (B64_MARK.length())));

                    img.attr("src", prePublish(imgBase64, srcSuffix));
                }
            }
            content = doc.getElementsByTag("body").html();
        }
        article.setContent(convertBack(content));
    }


    public  String prePublish(String content, String srcSuffix){

        String completePath = getDir();
        String name = generateFileName(srcSuffix);
        String path = completePath+ File.separator+ name;

        try {
            ImageUtil.base64ToImg(content, completePath, name);
        }catch(Exception e){
            logger.warn("prePublish error, content="+content, e);
        }

        return path;
    }

    public String getDir(){
        SimpleDateFormat format = new SimpleDateFormat("yyyMMdd");
        String d = format.format(new Date());
        if(StringUtils.isBlank(DEFAULT_FILE_PATH)){
            return WebContext.getRequest().getSession().getServletContext().getRealPath("/uploadFile/img/" +d );
        }else{
            return DEFAULT_FILE_PATH+"img"+d;
        }
    }



    public String generateFileName(String suffix){
        UUID fileNameUUID = UUID.randomUUID();
        return fileNameUUID.toString().replace("-","")+"."+suffix;
    }

    public String toBase64(String content){
        Pattern p =  Pattern.compile("^data:image/(png|gif|jpg|jpeg|bmp|tif|psd|ICO);base64,.*");
        String newContent = "";
        if(StringUtils.isNotBlank(content)){

            content = convertForth(content);
            Document doc = Jsoup.parse(content,"utf-8");
            Elements imgs = doc.getElementsByTag("img");

            for(Element img : imgs){

                String src = img.attr("src");
                if(p.matcher(src).find()){
                    logger.warn("文章中存在base64图片信息");
                    continue;
                }
                if(src.startsWith("http") || src.startsWith("https")){
                    logger.warn("文章中存在外部引用图片");
                    continue;
                }

                String extension = src.substring(src.lastIndexOf('.')+1);
                String base64 = "";
                try {
                    base64 = ImageUtil.imgToBase64(src);
                }catch(Exception e){
                    logger.warn("toBase64 failed: src="+src, e);
                }

                String prefix = "data:image/"+extension+";base64,";

                String srcVal = prefix+base64;
                img.attr("src", srcVal);
            }
            newContent = doc.getElementsByTag("body").html();
        }
        return newContent;
    }



    public static void main(String[] args) {


        Article ar = new Article();
        ar.setId(1L);
        ar.setTitle("通知1");
        ar.setAuthorId(1L);
        ar.setAuthorName("A");
        ar.setTypeId(5L);
        ar.setPublishTm(new Date());
        ar.setTop(1);
        ar.setSpeed(3);
        ar.setTypeName("通知");
        ar.setPublishStatus(1);
        ar.setCreateBy(0L);
        ar.setCreateTm(new Date());
        ar.setUpdateBy(0L);
        ar.setUpdateTm(new Date());
        ar.setVersion(1);

        ArticleType  type = new ArticleType();
        type.setId(1L);
        type.setTypeName("时事变量");
        type.setCreateBy(0L);
        type.setCreateTm(new Date());
        type.setUpdateBy(0L);
        type.setUpdateTm(new Date());
        type.setVersion(0);

    }

}
