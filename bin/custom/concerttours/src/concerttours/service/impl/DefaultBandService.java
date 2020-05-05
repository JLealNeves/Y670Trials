package concerttours.service.impl;

import concerttours.daos.BandDAO;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;

public class DefaultBandService implements BandService {

    private BandDAO bandDAO;

    @Override
    public List<BandModel> getBands() {
        return bandDAO.findBands();
    }

    @Override
    public BandModel getBandForCode(String code) throws AmbiguousIdentifierException, UnknownIdentifierException {
        final List<BandModel> result = bandDAO.findBandsByCode(code);
        if(result.isEmpty()){
            throw new UnknownIdentifierException("Band with code '" + code + "' not found!");
        }
        else if (result.size() > 1)
        {
            throw new AmbiguousIdentifierException("Band code '" + code + "' is not unique, " + result.size() + " bands found!");
        }
        return result.get(0);
    }

    public BandDAO getBandDAO() { return bandDAO; }

    public void setBandDAO(BandDAO bandDAO) { this.bandDAO = bandDAO; }
}
