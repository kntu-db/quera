import {Link} from "react-router-dom";
import {getHistory} from "../tasks/history";
import {currencyFormat} from "../utils/format";
import {useTheme} from "../hooks/theme";
import ThemeButton from "../components/them-button";
import "../assets/css/home.css"

export default function Home() {
    const [theme, builder] = useTheme();
    return (
        <div className={builder('home')}>
            <div className="button-container">
                <ThemeButton className="change-theme" />
            </div>
            <h1 className="text-header">Search &<br/> Buy <span>Crypto</span></h1>
            <p className="text">Shahid Beheshti University</p>
            <p className="text">IE: Final Project</p>
            <Link to="search" className="btn">SEARCH MORE</Link>
            <div className="cryto-blocks">
                {
                    getHistory().map(coin => (
                        <Link key={coin.id} to={`search/${coin.id}`} className="coin">
                            <img src={coin.image} alt={coin.name}/>
                            <div className="value">
                                <h4>{currencyFormat(coin.current_price)}</h4>
                                <p className="cryto-name">{coin.name}</p>
                            </div>
                        </Link>
                    ))
                }
            </div>
        </div>
    );
}