package com.fastkit.xmlresolver.css;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fastkit.xmlresolver.context.XMLElementContext;
import com.phloc.css.CSSSourceLocation;
import com.phloc.css.decl.CSSDeclaration;
import com.phloc.css.decl.CSSExpression;
import com.phloc.css.decl.CSSExpressionMemberTermSimple;
import com.phloc.css.decl.CSSExpressionMemberTermURI;
import com.phloc.css.decl.CSSFontFaceRule;
import com.phloc.css.decl.CSSImportRule;
import com.phloc.css.decl.CSSMediaQuery;
import com.phloc.css.decl.CSSMediaRule;
import com.phloc.css.decl.CSSPageRule;
import com.phloc.css.decl.CSSSelector;
import com.phloc.css.decl.CSSStyleRule;
import com.phloc.css.decl.CSSURI;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.decl.ICSSTopLevelRule;
import com.phloc.css.decl.visit.CSSVisitor;
import com.phloc.css.decl.visit.DefaultCSSUrlVisitor;
import com.phloc.css.decl.visit.DefaultCSSVisitor;
import com.phloc.css.utils.CSSDataURL;
import com.phloc.css.writer.CSSWriterSettings;
import com.woshidaniu.basicutils.ArrayUtils;
import com.woshidaniu.basicutils.BlankUtils;

/**
 * @package com.fastkit.css.parser
 * @className: CSSVisiter
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-8
 * @time : 下午7:02:58
 */

public class XMLCSSVisitor {
	
	protected static Logger LOG = LoggerFactory.getLogger(XMLCSSVisitor.class);

	private static XMLCSSVisitor instance = null;
	private CSSWriterSettings cssSetting = null;

	private XMLCSSVisitor() {
		cssSetting = XMLCSSResolver.getInstance().getCssSetting();
	}

	public static XMLCSSVisitor getInstance() {
		if (instance == null) {
			instance = new XMLCSSVisitor();
		}
		return instance;
	}

	public void visitSheet(CascadingStyleSheet css) {
		if(!BlankUtils.isBlank(css)){
			//先解析文本css
			CSSVisitor.visitCSS(css, new DefaultCSSVisitor() {
				
				@Override
				public void onEndPageRule(CSSPageRule aPageRule) {
					super.onEndPageRule(aPageRule);
					//Map<String,String> page = XMLCSSVisitor.getInstance().visitPage(aPageRule);
					//XMLElementContext.addPage(page);
				}

				@Override
				public void onEndFontFaceRule(CSSFontFaceRule aFontFaceRule) {
					super.onEndFontFaceRule(aFontFaceRule);
					//Map<String,String> fontFace = XMLCSSVisitor.getInstance().visitFontFace(aFontFaceRule);
					//XMLElementContext.addFontFace(fontFace.get("font-family"), fontFace);
				}
				
				@Override
				public void onEndMediaRule(CSSMediaRule aMediaRule) {
					super.onEndMediaRule(aMediaRule);
					//XMLCSSVisitor.getInstance().visitMedia(aMediaRule);
				}
				
				@Override
				public void onEndStyleRule(CSSStyleRule aStyleRule) {
					super.onEndStyleRule(aStyleRule);
					Map<String,Object> style = XMLCSSVisitor.getInstance().visitStyle(aStyleRule);
					List<CSSSelector> selectors = aStyleRule.getAllSelectors();
					for (CSSSelector cssSelector : selectors) {
						String member = cssSelector.getAsCSSString(cssSetting, 0);
						XMLElementContext.addStyle(member,style);
					}
				}
				
				// Called for each import
				@Override
				public void onImport(CSSImportRule importRule) {
					super.onImport(importRule);
					try {
						XMLCSSResolver.getInstance().resolveURI(importRule.getLocationString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 
	 * @description: 解析@font-face
	 * @font-face是CSS3中的一个模块，他主要是把自己定义的Web字体嵌入到你的网页中， 
	 *                                                随着@font-face模块的出现，我们在Web的开发中使用字体不怕只能使用Web安全字体
	 * @author : kangzhidong
	 * @date : 2014-1-10
	 * @time : 下午5:08:28
	 * @param aFontFaceRule
	 * @return
	 */
	public Map<String, String> visitFontFace(CSSFontFaceRule aFontFaceRule) {
		//log.info("================visitFontFace===================");
		final Map<String, String> font_face = new HashMap<String, String>();
		CSSVisitor.visitFontFaceRule(aFontFaceRule, new DefaultCSSVisitor() {
			@Override
			public void onDeclaration(CSSDeclaration aDeclaration) {
				//System.out.println(aDeclaration.getProperty() + ":" + visitExpression(aDeclaration.getExpression()));
				font_face.put(aDeclaration.getProperty(), visitExpression(aDeclaration.getExpression()));
			}
		});
		return font_face;
	}

	public Map<String,Object> visitPage(CSSPageRule aPageRule) {
		//log.info("================visitPage===================");
		final Map<String,Object> page = new HashMap<String,Object>();
		CSSVisitor.visitPageRule(aPageRule, new DefaultCSSVisitor() {
			@Override
			public void onDeclaration(CSSDeclaration aDeclaration) {
				//System.out.println(aDeclaration.getProperty() + ":" + visitExpression(aDeclaration.getExpression()));
				page.put(aDeclaration.getProperty(), visitExpression(aDeclaration.getExpression()));
			}
		});
		return page;
	}

	public Map<String, Map<String,Object>> visitMedia(CSSMediaRule aMediaRule) {
		//log.info("================visitMedia===================");
		Map<String, Map<String,Object>> media = new HashMap<String, Map<String,Object>>();
		CSSVisitor.visitMediaRule(aMediaRule, new DefaultCSSVisitor() {

			@Override
			public void onEndMediaRule(CSSMediaRule aMediaRule) {
				Map<String, Map<String,Object>> css = new HashMap<String, Map<String,Object>>();

				Map<String,Object> madia = null;
				for (CSSMediaQuery cssMediaQuery : aMediaRule.getAllMediaQueries()) {
					madia = new HashMap<String,Object>();
					String selector = cssMediaQuery.getAsCSSString(cssSetting,0);
					//System.out.println("selector:" + selector);
					/*
					 * cssMediaQuery.isNot() cssMediaQuery.isOnly()
					 * cssMediaQuery.getMedium()
					 */
					List<ICSSTopLevelRule> ruleList = aMediaRule.getAllRules();
					for (ICSSTopLevelRule rule : ruleList) {
						//System.out.println(rule.getAsCSSString(cssSetting, 0));
					}
					break;
				}
			}
		});
		return media;
	}

	public Map<String, Object> visitStyle(CSSStyleRule aStyleRule) {
		LOG.info("================visitStyle===================");
		final Map<String, Object> styles = new HashMap<String, Object>();
		CSSVisitor.visitStyleRule(aStyleRule, new DefaultCSSVisitor() {
			@Override
			public void onDeclaration(CSSDeclaration aDeclaration) {
				LOG.info(aDeclaration.getProperty()+":"+visitExpression(aDeclaration.getExpression()));
				styles.put(aDeclaration.getProperty(),visitExpression(aDeclaration.getExpression()));
			}
		});
		//解析css中的路径
		CSSVisitor.visitAllDeclarationUrls(aStyleRule, new DefaultCSSUrlVisitor() {

			// Call for URLs outside of URLs
			@Override
			public void onUrlDeclaration(ICSSTopLevelRule aTopLevelRule,
					CSSDeclaration aDeclaration,
					CSSExpressionMemberTermURI aURITerm) {
				super.onUrlDeclaration(aTopLevelRule, aDeclaration, aURITerm);
				CSSURI uri = aURITerm.getURI();
				if (uri.isDataURL()) {
					CSSDataURL aDataURL = uri.getAsDataURL();
					try {
						BufferedImage image = ImageIO.read(new ByteArrayInputStream(aDataURL.getContentBytes()));
						styles.put(aDeclaration.getProperty(), image);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					styles.put(aDeclaration.getProperty(), uri.getURI());
				}
			}
		});
		return styles;
	}

	public static String visitExpression(CSSExpression expression) {
		List<CSSExpressionMemberTermSimple> list = expression.getAllSimpleMembers();
		String[] array = new String[list.size()];
		for (int i = 0; i < array.length; i++) {
			CSSExpressionMemberTermSimple termSimple = list.get(i);
			array[i] = termSimple.getOptimizedValue();// termSimple.getValue()
		}
		return array.length > 1 ? ArrayUtils.toString(array).replace("{", "").replace("}", ""): (array.length > 0 ? array[0] : "");
	}

	public static String visitSourceLocation(CSSSourceLocation location) {
		return "source location reaches from ["
				+ location.getFirstTokenEndLineNumber() + "/"
				+ location.getFirstTokenEndColumnNumber() + "] up to ["
				+ location.getLastTokenEndLineNumber() + "/"
				+ location.getLastTokenEndColumnNumber() + "]";
	}
}
