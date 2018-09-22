/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.services.extractor.internal;

import java.util.ArrayList;
import java.util.List;

import bull.services.extractor.IndicatorsExtractor;
import bull.internal.utils.UpsourceUtils;
import bull.internal.issues.Upsource;

/**
 * Class that extracts the data from Upsource to store it in a list of upsource objects.
 *
 * @author Bull/Atos
 */
public class UpsourceExtractImpl implements IndicatorsExtractor<Upsource> {
    private UpsourceUtils tools;


    public UpsourceExtractImpl(){
        this.tools = new UpsourceUtils();
    }

    /**
     * Uses the UpsourceUtils class to extract all the upsource's data members.
     * @return List of revisions
     */
    public List<Upsource> fetch() {
        List<Upsource> upsourceData = new ArrayList<>();

        this.tools.revisionsListFiltered(upsourceData);
        this.tools.revisionReviewInfo(upsourceData);
        this.tools.revisionBranches(upsourceData);
        this.tools.reviews(upsourceData);
        return upsourceData;
    }
}
