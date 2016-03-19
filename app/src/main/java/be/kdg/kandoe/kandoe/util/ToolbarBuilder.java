package be.kdg.kandoe.kandoe.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.LandingActivity;
import be.kdg.kandoe.kandoe.activity.MainActivity;
import be.kdg.kandoe.kandoe.activity.OrganisationActivity;
import be.kdg.kandoe.kandoe.dom.User;

/**
 * Created by Edward on 17/03/2016.
 */
public class ToolbarBuilder {
    private static Toolbar toolbar;
    private static Drawer drawer;

    public static Drawer makeDefaultDrawer(final AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_background1)
                .withSelectionListEnabled(false)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(AccountSettings.getLoggedInUser().getUsername())
                                .withEmail(AccountSettings.getLoggedInUser().getEmail())
                                .withEnabled(true)
                                .withIcon(new IconDrawable(activity, FontAwesomeIcons.fa_reddit))
                                .withIcon(AccountSettings.getLoggedInUser().getImageUrl())
                ).build();

        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Change organisation").withIcon(new IconDrawable(activity, FontAwesomeIcons.fa_users)).withDescription("current: TODO"),
                        new PrimaryDrawerItem().withName("Change theme").withIcon(new IconDrawable(activity, FontAwesomeIcons.fa_pie_chart)).withDescription("current: TODO"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Chat").withIcon(new IconDrawable(activity, FontAwesomeIcons.fa_comment))
                                .withOnDrawerItemClickListener(gotoFragmentListener(1))
                                .withIdentifier(1),
                        new PrimaryDrawerItem().withName("Game").withIcon(new IconDrawable(activity, FontAwesomeIcons.fa_dot_circle_o))
                                .withOnDrawerItemClickListener(gotoFragmentListener(2))
                                .withIdentifier(2),
                        new PrimaryDrawerItem().withName("Cards").withIcon(new IconDrawable(activity, FontAwesomeIcons.fa_files_o))
                                .withOnDrawerItemClickListener(gotoFragmentListener(3))
                                .withIdentifier(3)
                )
                .build();
        drawer.addStickyFooterItem(new PrimaryDrawerItem()
                .withName("Log out").withOnDrawerItemClickListener(logOutActivity(activity))
                .withIcon(new IconDrawable(activity, FontAwesomeIcons.fa_sign_out).alpha(100)));

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        return drawer;
    }


    public static Drawer.OnDrawerItemClickListener gotoFragmentListener(final int fragmentPositionOnViewPager){
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                MainActivity.getInstance().getViewPager().setCurrentItem(fragmentPositionOnViewPager);
                return false;
            }
        };
    }
    public static Drawer.OnDrawerItemClickListener logOutActivity(final Activity activity){
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent myintent = new Intent(activity, LandingActivity.class);
                activity.startActivity(myintent);
                activity.finish();
                return false;
            }
        };
    }

}
