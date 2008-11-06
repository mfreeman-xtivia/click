package net.sf.clickide.ui.editor.forms;

import net.sf.clickide.ClickPlugin;
import net.sf.clickide.ClickUtils;
import net.sf.clickide.ui.editor.actions.ElementAppendAction;
import net.sf.clickide.ui.editor.attrs.IAttributeEditor;
import net.sf.clickide.ui.editor.attrs.ServiceClassNameAttributeEditor;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

/**
 *
 * @author Naoki Takezoe
 * @since 2.1.0
 */
public class ClickServiceEditor extends AbstractMasterDetailEditor {

	protected void createMenu(IDOMElement element) {
		if(element.getNodeName().equals(ClickPlugin.TAG_CLICK_APP)){
			if(ClickUtils.getElement(element, ClickPlugin.TAG_LOG_SERVICE)==null){
				newMenu.add(new ElementAppendAction(ClickPlugin.TAG_LOG_SERVICE, element, null, this));
			}
			if(ClickUtils.getElement(element, ClickPlugin.TAG_TEMPLATE_SERVICE)==null){
				newMenu.add(new ElementAppendAction(ClickPlugin.TAG_TEMPLATE_SERVICE, element, null, this));
			}
		}
	}

	protected String[] getAcceptElementNames() {
		return new String[]{ClickPlugin.TAG_LOG_SERVICE, ClickPlugin.TAG_TEMPLATE_SERVICE};
	}

	protected IAttributeEditor getAttributeEditor(String elementName) {
		if(elementName.equals(ClickPlugin.TAG_LOG_SERVICE)){
			return new ServiceClassNameAttributeEditor("net.sf.click.service.LogService");
		}
		if(elementName.equals(ClickPlugin.TAG_TEMPLATE_SERVICE)){
			return new ServiceClassNameAttributeEditor("net.sf.click.service.TemplateService");
		}
		return null;
	}

}
