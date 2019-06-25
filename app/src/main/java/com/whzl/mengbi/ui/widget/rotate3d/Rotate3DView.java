package com.whzl.mengbi.ui.widget.rotate3d;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.util.glide.GlideImageLoader;

/**
 * @author nobody
 * @date 2019-06-18
 */
public class Rotate3DView extends ConstraintLayout {

    private final Context context;
    private Rotate3D anim;

    public Rotate3D getAnim() {
        return anim;
    }

    private ImageView ivPic;
    private TextView tvName;

    public Rotate3DView(Context context) {
        this(context, null);
    }

    public Rotate3DView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Rotate3DView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_rotate_3d, this, false);
        ivPic = view.findViewById(R.id.iv_pic);
        ImageView ivBackgroud = view.findViewById(R.id.iv_backgroud);
        ConstraintLayout constraintLayout = view.findViewById(R.id.cl_flop);
        tvName = view.findViewById(R.id.tv_name);
        addView(view);

        anim = new Rotate3D.Builder(context)
                .bindParentView(this)
                .bindPositiveView(ivBackgroud)
                .bindNegativeView(constraintLayout)
                .create();
    }

    public void transform() {
        anim.transform();
    }

    public void setOpen(boolean open) {
        anim.setIsOpen(open);
    }

    public void setIvPic(Object object) {
        GlideImageLoader.getInstace().displayImage(context, object, ivPic);
    }

    public void setTvName(String name) {
        tvName.setText(name);
    }

}
