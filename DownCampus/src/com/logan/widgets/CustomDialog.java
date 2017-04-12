package com.logan.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.mobilecampus.R;

/**
 * Created by Z2z on 2017/4/8.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private DialogInterface.OnClickListener closeListener;
        private DialogInterface.OnClickListener studentListener;
        private DialogInterface.OnClickListener parentListener;
        private DialogInterface.OnClickListener teacherListener;
        private DialogInterface.OnClickListener leaderListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @return
         */
        public Builder setcloseButton(DialogInterface.OnClickListener listener) {
            this.closeListener = listener;
            return this;
        }

        public Builder setstudentButton(DialogInterface.OnClickListener listener) {
            this.studentListener = listener;
            return this;
        }

        public Builder setparentButton(DialogInterface.OnClickListener listener) {
            this.parentListener = listener;
            return this;
        }

        public Builder setteacherButton(DialogInterface.OnClickListener listener) {
            this.teacherListener = listener;
            return this;
        }

        public Builder setleaderButton(DialogInterface.OnClickListener listener) {
            this.leaderListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_custom, null);
            final CustomDialog dialog = new CustomDialog(context,R.style.custom_dialog);
            dialog.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                    .WRAP_CONTENT));
            if (closeListener != null) {
                ((ImageView) view.findViewById(R.id.close)).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }else
                view.findViewById(R.id.close).setVisibility(View.GONE);

            if (studentListener != null) {
                ((ImageView) view.findViewById(R.id.student)).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }else
                ((ImageView) view.findViewById(R.id.student)).setVisibility(View.GONE);

            if (teacherListener != null) {
                ((ImageView) view.findViewById(R.id.teacher)).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        teacherListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }else
                ((ImageView) view.findViewById(R.id.teacher)).setVisibility(View.GONE);

            if (parentListener != null) {
                ((ImageView) view.findViewById(R.id.parent)).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }else
                ((ImageView) view.findViewById(R.id.parent)).setVisibility(View.GONE);

            if (leaderListener != null) {
                ((ImageView) view.findViewById(R.id.leader)).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leaderListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }else
                ((ImageView) view.findViewById(R.id.leader)).setVisibility(View.GONE);

            dialog.setContentView(view);
            return dialog;
        }
    }

}
