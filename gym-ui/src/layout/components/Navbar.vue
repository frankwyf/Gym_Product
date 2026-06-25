<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav" />
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />

        <!-- <screenfull id="screenfull" class="right-menu-item hover-effect" /> -->

        <!-- <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip> -->

      </template>

      <el-dropdown class="right-menu-item hover-effect" trigger="click">
        <span class="lang-trigger">{{ $tr('navbar.language') }}</span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="changeLocale('en')">{{ $tr('lang.english') }}</el-dropdown-item>
          <el-dropdown-item @click.native="changeLocale('zh')">{{ $tr('lang.chinese') }}</el-dropdown-item>
          <el-dropdown-item @click.native="changeLocale('ja')">{{ $tr('lang.japanese') }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>{{ $tr('navbar.personalCenter') }}</el-dropdown-item>
          </router-link>
<!--          <el-dropdown-item @click.native="setting = true">-->
<!--            <span>Layout Settings</span>-->
<!--          </el-dropdown-item>-->
<!--          <el-dropdown-item v-hasRole="['teacher']" @click.native="toStudent">-->
<!--            <span>My students </span>-->
<!--          </el-dropdown-item>-->
<!--          <el-dropdown-item v-hasRole="['member']" @click.native="toCoach">-->
<!--            <span>My coachs </span>-->
<!--          </el-dropdown-item>-->
<!--          <el-dropdown-item v-hasRole="['teacher']" @click.native="toAssignStudent">-->
<!--            <span>Assign students </span>-->
<!--          </el-dropdown-item>-->
<!--          <el-dropdown-item v-hasRole="['teacher']" @click.native="toAssignGym">-->
<!--            <span>Assign gyms </span>-->
<!--          </el-dropdown-item>-->
          <el-dropdown-item divided @click.native="logout">
            <span>{{ $tr('navbar.logout') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  methods: {
    changeLocale(locale) {
      this.$i18n.setLocale(locale)
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    toStudent(){
      var str = "/operation/view/student/" + this.$store.state.user.userId;
      console.log(str);
      this.$router.push(str);
    },
    toAssignStudent(){
      var str = "/operation/assignment/student/" + this.$store.state.user.userId;
      console.log(str);
      this.$router.push(str);
    },
    toAssignGym(){
      var str = "/operation/view/rent";
      console.log(str);
      this.$router.push(str);
    },
    toCoach(){
      var str = "/operation/view/teacher/" + this.$store.state.user.userId;
      console.log(str);
      this.$router.push(str);
    },
    async logout() {
      this.$confirm(this.$tr('navbar.confirmLogoutContent'), this.$tr('navbar.confirmLogoutTitle'), {
        confirmButtonText: this.$tr('common.confirm'),
        cancelButtonText: this.$tr('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {});
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
