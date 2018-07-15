package com.wenwo.web.admin;

import com.wenwo.config.SiteConfig;
import com.wenwo.core.base.BaseController;
import com.wenwo.core.bean.Result;
import com.wenwo.core.exception.ApiAssert;
import com.wenwo.core.util.FileType;
import com.wenwo.core.util.FileUtil;
import com.wenwo.module.attachment.model.Attachment;
import com.wenwo.module.tag.model.Tag;
import com.wenwo.module.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/tag")
public class TagAdminController extends BaseController {

  @Autowired
  private TagService tagService;
  @Autowired
  private SiteConfig siteConfig;
  @Autowired
  private FileUtil fileUtil;

  @GetMapping("/list")
  public String list(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
    Page<Tag> page = tagService.page(pageNo, siteConfig.getPageSize());
    model.addAttribute("page", page);
    return "admin/tag/list";
  }

  @GetMapping("/edit")
  public String edit(Integer id, Model model) {
    model.addAttribute("tag", tagService.findById(id));
    return "admin/tag/edit";
  }

  /**
   * 更新标签
   * @param id
   * @param intro
   * @param file
   * @return
   */
  @PostMapping("/edit")
  public String update(Integer id, String intro, MultipartFile file) {
    Attachment attachment = null;
    try {
      if (siteConfig.getUploadType().equals("local")) {
        attachment = fileUtil.uploadFile(file, FileType.PICTURE, "admin_tag");
      } else if (siteConfig.getUploadType().equals("qiniu")) {
        attachment = fileUtil.uploadFileToQiniu(file, FileType.PICTURE);
      }
      Tag tag = tagService.findById(id);
      tag.setIntro(intro);
      tag.setLogo(attachment != null ? attachment.getRequestUrl() : null);
      tagService.save(tag);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return redirect("/admin/tag/list");
  }

  /**
   * 删除标签，只能删除没有话题关联的标签
   * @param id
   * @return
   */
  @GetMapping("/delete")
  @ResponseBody
  public Result delete(Integer id) {
    Tag tag = tagService.findById(id);
    ApiAssert.notTrue(tag.getTopicCount() > 0, "这个标签还有关联的话题，请先处理话题内标签再来删除");

    tagService.delete(tag);
    return Result.success();
  }
}
