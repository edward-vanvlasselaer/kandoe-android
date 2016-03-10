package be.kdg.kandoe.kandoe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.CustomPagerAdapter;
import be.kdg.kandoe.kandoe.dom.User;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Drawer drawer;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initTabLayout(); --> needed?
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViewPager();
        initMaterialDrawer();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),3);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if(drawer != null && drawer.isDrawerOpen())
            drawer.closeDrawer();
        else
            super.onBackPressed();
    }
    private void initMaterialDrawer() {


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_background1)
                .withAlternativeProfileHeaderSwitching(false)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withIsExpanded(false)
                                .withName(User.getLoggedInUser().getUsername())
                                .withEmail("emailhere.com")
                                .withEnabled(false)
                                .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_reddit))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {

                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Chat").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_comment)),
                        new PrimaryDrawerItem().withName("Game").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_dot_circle_o)),
                        new PrimaryDrawerItem().withName("Cards").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_files_o)),
                        new SectionDrawerItem().withName("SECTION"),
                        new SecondaryDrawerItem().withName("TODO").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new SecondaryDrawerItem().withName("TODO").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new SecondaryDrawerItem().withName("TODO").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out)),
                        new SecondaryDrawerItem().withName("TODO").withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
                        }
                        //true: do nothing
                        //false: close drawer
                        return true;
                    }
                }).build();

        drawer.addStickyFooterItem(new PrimaryDrawerItem()
                        .withName("Log out")
                        .withIcon(new IconDrawable(this, FontAwesomeIcons.fa_sign_out).alpha(100)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }
}