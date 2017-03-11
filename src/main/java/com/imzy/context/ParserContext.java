package com.imzy.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.imzy.config.Const;
import com.imzy.utils.ReflectUtil;
import com.imzy.xml.node.DmlNode;
import com.imzy.xml.node.LangType;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.SqlNode;
import com.imzy.xml.node.parser.Parsable;
import com.imzy.xml.structure.TreeNode;

/**
 * xml解析器上下文
 * @author yangzhang7
 *
 */
public class ParserContext {

	private static ParserContext parserContext = new ParserContext();

	private static Map<String, Parsable<?>> placeholerParsers = new HashMap<String, Parsable<?>>();
	private static Map<String, Parsable<?>> textParsers = new HashMap<String, Parsable<?>>();
	private static Map<String, Parsable<?>> commonParsers = new HashMap<String, Parsable<?>>();

	private static Map<String, Map<String, Parsable<?>>> allParser = new HashMap<String, Map<String, Parsable<?>>>();

	private static Configuration config = null;
	static {
		try {
			config = new PropertiesConfiguration(Const.PARSER_PROPERTIES_PATH);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		allParser.put("text", textParsers);
		allParser.put("placeholder", placeholerParsers);
		allParser.put("common", commonParsers);
	}

	public static ParserContext getParserContext() {
		return parserContext;
	}

	private ParserContext() {
	}

	@SuppressWarnings("unchecked")
	private <T> void fillParserMap(Map<String, T> parserMap, String prefix) {
		try {
			Iterator<String> parserIterator = config.getKeys(prefix);
			while (parserIterator.hasNext()) {
				String key = parserIterator.next();
				String parserClass = config.getString(key);
				Class<T> clazz = (Class<T>) Class.forName(parserClass);
				T parser = clazz.newInstance();
				parserMap.put(key.substring(key.lastIndexOf(".") + 1), parser);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化解析器
	 */
	public void init() {
		fillParserMap(textParsers, "parser.text");
		fillParserMap(placeholerParsers, "parser.placeholder");
		fillParserMap(commonParsers, "parser.common");
	}

	/**
	 * 获取解析器
	 * @param qName 解析器类型
	 * @param langType 语言类型
	 * @return
	 */
	public static Parsable<?> getParserByQNameAndLang(String qName, String langType) {
		qName = qName.toLowerCase().trim();

		Set<String> commonParsersKeySet = commonParsers.keySet();
		Map<String, Parsable<?>> qNameParsers = null;
		Parsable<?> obj = null;
		if (commonParsersKeySet.contains(qName)) {
			qNameParsers = allParser.get("common");
			obj = qNameParsers.get(qName);
		} else {
			qNameParsers = allParser.get(qName);
			obj = qNameParsers.get(langType);
		}
		return obj;
	}

	/**
	 * 获取公共解析器
	 * @param qName 解析器名称
	 * @return
	 */
	public static Parsable<?> getCommonParserByQName(String qName) {
		return getParserByQNameAndLang(qName, null);
	}

	/**
	 * 获取textParser
	 * @param node 当前节点
	 * @return
	 */
	public static Parsable<?> getTextParser(Node node) {
		// 首先根据node获取到树
		TreeNode<Node> tree = TreeContext.getTreeNode(node.getTreeId());
		// 再根据树获取当前节点在树中位置
		TreeNode<Node> self = tree.findTreeNodeById(node.getSelfId());

		// 最后获取父为sqlnode或dmlnode的节点
		List<TreeNode<?>> elders = self.getEldersByTypes(DmlNode.class, SqlNode.class);
		for (TreeNode<?> elder : elders) {
			LangType langType = (LangType) ReflectUtil.getFieldValue(elder.getData(), "langType");
			return getParserByQNameAndLang("text", langType.getName());
		}
		return null;

	}
}
