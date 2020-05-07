package concerttours.facades.impl;

import concerttours.data.ConcertSummaryData;
import concerttours.data.TourData;
import concerttours.enums.ConcertType;
import concerttours.facades.TourFacade;
import concerttours.model.ConcertModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.internal.cglib.asm.$AnnotationVisitor;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class DefaultTourFacade implements TourFacade {

    private ProductService productService;

    @Override
    public TourData getTourDetails(String tourId) {
        if(tourId == null){
            throw new IllegalArgumentException("Tour id cannot be null");
        }

        final ProductModel productModel = productService.getProductForCode(tourId);
        if(productModel == null){
            return null;
        }

        final List<ConcertSummaryData> concerts = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(productModel.getVariants())){
            for(final VariantProductModel variant : productModel.getVariants()){
                if(variant instanceof ConcertModel){
                    final ConcertModel concert = (ConcertModel) variant;
                    final ConcertSummaryData summary = new ConcertSummaryData();
                    summary.setId(concert.getCode());
                    summary.setDate(concert.getDate());
                    summary.setVenue(concert.getVenue());
                    summary.setType(concert.getConcertType() == ConcertType.OPENAIR ? "Outdoors" : "Indoors");
                    summary.setCountDown(concert.getDaysUntil());
                    concerts.add(summary);
                }
            }
        }

        final TourData tourData = new TourData();
        tourData.setId(productModel.getCode());
        tourData.setTourName(productModel.getName());
        tourData.setDescription(productModel.getDescription());
        tourData.setConcerts(concerts);
        return tourData;
    }
    @Required
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
