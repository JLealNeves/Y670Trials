package concerttours.facades.impl;

import concerttours.data.BandData;
import concerttours.data.TourSummaryData;
import concerttours.enums.MusicType;
import concerttours.facades.BandFacade;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.spockframework.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class DefaultBandFacade implements BandFacade {

    private BandService bandService;

    @Override
    public BandData getBand(String name) {
        if(name == null){
            throw new IllegalArgumentException("Band name cannot be null");
        }
        final BandModel band = bandService.getBandForCode(name);
        if(band == null){
            return null;
        }

        final List<String> genres = new ArrayList<>();
        if(band.getTypes() != null){
            for(final MusicType music: band.getTypes()){
                genres.add(music.getCode());
            }
        }

        final List<TourSummaryData> tourSummaryData = new ArrayList<>();
        if(band.getTours() != null){
            for(final ProductModel tour : band.getTours()){
                final TourSummaryData tsd = new TourSummaryData();
                tsd.setId(tour.getCode());
                tsd.setTourName(tour.getName());
                tsd.setNumberOfConcerts(Integer.toString(tour.getVariants().size()));
                tourSummaryData.add(tsd);
            }
        }

        final BandData bandData = new BandData();
        bandData.setId(band.getCode());
        bandData.setName(band.getName());
        bandData.setAlbumsSold(band.getAlbumSales());
        bandData.setDescription(band.getHistory());
        bandData.setGenres(genres);
        bandData.setTours(tourSummaryData);

        return bandData;
    }

    @Override
    public List<BandData> getBands() {
        final List<BandModel> bandModels = bandService.getBands();
        final List<BandData> bandData = new ArrayList<>();

        for(final BandModel bm : bandModels){
            final BandData sfd = new BandData();
            sfd.setId(bm.getCode());
            sfd.setName(bm.getName());
            sfd.setDescription(bm.getHistory());
            sfd.setAlbumsSold(bm.getAlbumSales());
            bandData.add(sfd);
        }
        return bandData;
    }

    @Required
    public void setBandService(BandService bandService) {
        this.bandService = bandService;
    }
}
