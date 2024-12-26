package gr.grumpy.odftoolkittool;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.odftoolkit.odfdom.dom.OdfSchemaDocument;
import org.odftoolkit.odfdom.dom.attribute.fo.FoKeepTogetherAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleMayBreakBetweenRowsAttribute;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.pkg.OdfFileDom;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.w3c.dom.DOMException;

public class OdftoolkittoolTest {

    @org.junit.jupiter.api.Test
    public void testMain() throws Exception {
    
        InputStream is = OdftoolkittoolTest.class.getClassLoader().getResourceAsStream("test.odt");
        TextDocument textDocument = TextDocument.loadDocument(is);

        Table tbl = textDocument.getTableByName("Table2");
        if (tbl == null) {
            tbl = Table.newTable(textDocument);
        }

        applyMayBreakBetweenRowsAttributeToTable(tbl);

        // append as many rows as is necessary to fill the whole page
        int n = 33;
        Row currentRow = null;
        do {
            currentRow = tbl.appendRow();
        } while (--n >= 0);
        
        // add some text that should last until the next page break
        String text = "asdasdasdasd asdasda sdasf aspfoasjfkasjflaksj flaksf";
        currentRow.getCellByIndex(0).setDisplayText(text);
        
        // this sets the "keep together" attribute to "always" for all existing rows
        for (Row r : tbl.getRowList()) {
            // applyKeepTogetherAttributeToRow(r);
            applyStyleNameAttributeToRow(r, "Table2.1");
        }
        
        // save the new document to inspect it after test
        File targetFile = new File("c:\\temp\\lala.odt");
        textDocument.save(targetFile);
    }

    // this somehow sets the "allow table to be split across pages", and not the "allow row to be split across pages"
    private void applyMayBreakBetweenRowsAttributeToTable(Table table) throws Exception, DOMException {
        OdfFileDom parentDom = table.getOwnerDocument().getContentDom();
        StyleMayBreakBetweenRowsAttribute attribute =
                new StyleMayBreakBetweenRowsAttribute(parentDom);
        attribute.setBooleanValue(true);
        table.getOdfElement().setAttributeNode(attribute);
        
        // there are multiple ways to set attributes - another one is to odfStyle.set
//        OdfStyle odfStyle = table.getStyleHandler().getStyleElementForWrite();
//        odfStyle.setAttributeNode(attribute);
    }

    // it seems that applying this attribute directly to an element does not do anything
    // it has to be applied to a style and then this style should be referenced in element
    private Row applyKeepTogetherAttributeToRow(Row r) throws Exception {
        OdfFileDom parentDom = r.getOwnerDocument().getContentDom();
        FoKeepTogetherAttribute attribute = new FoKeepTogetherAttribute(parentDom);
        attribute.setEnumValue(FoKeepTogetherAttribute.Value.ALWAYS);
        r.getOdfElement().setOdfAttribute(attribute);
        return r;
    }
    
    private Row applyStyleNameAttributeToRow(Row r, String styleName) throws Exception {
        OdfFileDom parentDom = r.getOwnerDocument().getContentDom();
        r.getOdfElement().setStyleName(styleName);
        return r;
    }    
}
