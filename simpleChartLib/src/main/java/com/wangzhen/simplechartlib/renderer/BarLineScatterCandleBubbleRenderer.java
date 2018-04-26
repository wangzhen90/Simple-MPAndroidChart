package com.wangzhen.simplechartlib.renderer;

import com.wangzhen.simplechartlib.data.dataSet.DataSet;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.interfaces.charts.BarLineScatterCandleBubbleDataProvider;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarLineScatterCandleBubbleDataSet;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/4/26.
 */

public abstract class BarLineScatterCandleBubbleRenderer extends DataRenderer {


    public BarLineScatterCandleBubbleRenderer(ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
    }

    /**
     * Class representing the bounds of the current viewport in terms of indices in the values array of a DataSet.
     * <p>
     * 用DataSet的values数组中的索引表示当前视口边界的类。
     */
    protected class XBounds {

        /**
         * minimum visible entry index
         */
        public int min;

        /**
         * maximum visible entry index
         */
        public int max;

        /**
         * range of visible entry indices
         * 可见entry的角标范围
         */
        public int range;

        /**
         * 计算x最小和最大值以及它们之间的范围。
         */

        public void set(BarLineScatterCandleBubbleDataProvider chart, IBarLineScatterCandleBubbleDataSet dataSet) {
            /**
             * TODO X值得动画阶段，这里先不做动画，所以为1
             */

            //        float phaseX = Math.max(0.f, Math.min(1.f, mAnimator.getPhaseX()));
            float phaseX = 1f;
            float low = chart.getLowestVisibleX();
            float high = chart.getHighestVisibleX();
            //不关注y值所以传入Float.NAN
            //其实位置如果不是整数，起始点向下取整，终点向上取整
            Entry entryFrom = dataSet.getEntryForXValue(low, Float.NaN, DataSet.Rounding.DOWN);
            Entry entryTo = dataSet.getEntryForXValue(high, Float.NaN, DataSet.Rounding.UP);

            min = entryFrom == null ? 0 : dataSet.getEntryIndex(entryFrom);
            max = entryTo == null ? 0 : dataSet.getEntryIndex(entryTo);
            range = (int) ((max - min) * phaseX);

        }
    }
}

