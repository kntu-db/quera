import {Route, Routes} from "react-router-dom";
import ListCoins from "./list-coins";
import CoinDetails from "./coin-details";
import NavBar from "../components/nav-bar";
import "../assets/css/search.css";

export default function Search() {
    return (
        <div className="search">
            <NavBar/>
            <div className="search-content">
                <Routes>
                    <Route path="/" element={<ListCoins/>}/>
                    <Route path="/:coinId" element={<CoinDetails/>}/>
                </Routes>
            </div>
        </div>
    )
}