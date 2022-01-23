package org.ogorodnik.shop.web.templater;

public class PageGeneratorCreator {
    private PageGenerator pageGenerator;

    public PageGenerator getPageGenerator() {
        if (pageGenerator == null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

}
