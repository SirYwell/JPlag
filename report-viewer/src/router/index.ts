import {createRouter, createWebHistory, RouteRecordRaw} from "vue-router";
import OverviewView from "@/views/OverviewView.vue"
import ComparisonView from "@/views/ComparisonView.vue"
import FileUploadView from "@/views/FileUploadView.vue"

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "FileUploadView",
        component: FileUploadView
    },
    {
        path: "/overview",
        name: "OverviewView",
        component: OverviewView,
    },
    {
        path: "/comparison",
        name: "ComparisonView",
        component: ComparisonView,
        props: route => ({
            firstId: route.query.firstId,
            secondId: route.query.secondId
        })
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
