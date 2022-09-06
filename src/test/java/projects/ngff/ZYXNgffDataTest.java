package projects.ngff;

import org.embl.mobie.io.ImageDataFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import bdv.img.cache.VolatileCachedCellImg;
import lombok.extern.slf4j.Slf4j;
import mpicbg.spim.data.SpimDataException;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.cell.CellGrid;
import projects.remote.BaseTest;

@Slf4j
public class ZYXNgffDataTest extends BaseTest {
    private static final String URL = "https://s3.embl.de/i2k-2020/ngff-example-data/v0.4/zyx.ome.zarr";
    private static final ImageDataFormat FORMAT = ImageDataFormat.OmeZarrS3;

    public ZYXNgffDataTest() throws SpimDataException {
        super(URL, FORMAT);
        //set values for base test
        setExpectedTimePoints(1);
        setExpectedShape(new FinalDimensions(483, 393, 603));
        setExpectedDType("uint8");
    }

    @Test
    public void checkDataset() {
        long x = 1;
        long y = 1;
        long z = 1;
        long[] imageDimensions = spimData.getSequenceDescription().getImgLoader().getSetupImgLoader(0).getImage(0).dimensionsAsLongArray();
        if (x > imageDimensions[0] || y > imageDimensions[1] || z > imageDimensions[2]) {
            throw new RuntimeException("Coordinates out of bounds");
        }

        RandomAccessibleInterval<?> realPixelValue = spimData.getSequenceDescription().getImgLoader().getSetupImgLoader(0).getImage(0);
        VolatileCachedCellImg volatileCachedCellImg = (VolatileCachedCellImg) realPixelValue;
        CellGrid cellGrid = volatileCachedCellImg.getCellGrid();
        long[] dims = new long[]{483, 393, 603};
        int[] cellDims = new int[]{64, 64, 64};
        CellGrid expected = new CellGrid(dims, cellDims);
        Assertions.assertEquals(expected, cellGrid);
    }
}
