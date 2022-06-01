import {useEffect, useState} from "react";
import {fetchAll, fetchSuggestions} from "../tasks/crypto";
import {currencyFormat, unitFormat} from "../utils/format"
import "../assets/css/font-awesome.css";
import {useNavigate} from "react-router-dom";
import {useTheme} from "../hooks/theme";

export default function ListCoins() {

    const navigate = useNavigate();
    const [coins, setCoins] = useState([]);
    const [suggestions, setSuggestions] = useState([]);
    const [input, setInput] = useState("");
    const [loadSuggestion, setLoadSuggestion] = useState(false);

    const [theme, builder] = useTheme();

    useEffect(() => {
        setLoadSuggestion(true);
        const timeout = setTimeout(() => {
            fetchSuggestions(input).then(res => {
                setLoadSuggestion(false);
                setSuggestions(res)
            }).catch(() => {
                setLoadSuggestion(false);
            })
        }, 600);
        return () => clearTimeout(timeout);
    }, [input])

    useEffect(() => {
        fetchAll(input, 25, 1).then(res => {
            setCoins([...coins, ...res]);
        })
    }, []);

    return (
        <div className={builder('list-coins')}>
            <h1 className="header">Search Coin</h1>
            <p>Get Information from here</p>
            <div className="container">
                <div className="header">
                    <p>Cryptocurrency Prices by Market Cap</p>
                    <form className="search-box" onSubmit={e => {
                        fetchAll(input, 25, 1).then(res => {
                            setCoins(res);
                        });
                        e.preventDefault();
                    }}>
                        <input
                            className="search-text"
                            placeholder="Search For a Crypto Currency ..."
                            type="text"
                            onChange={e => setInput(e.target.value)}
                            list="suggestions"
                        />
                        <datalist id="suggestions">
                            {
                                suggestions.map(suggestion => (
                                    <option key={suggestion.id} value={suggestion.id} />
                                ))
                            }
                        </datalist>
                        {
                            loadSuggestion ?
                                <i className="fa fa-spinner fa-spin icon"/> :
                                <input className="icon" type="submit" value="&#xf002;"/>
                        }
                    </form>
                </div>
                <div className="table-container">
                    <table>
                        <thead>
                        <tr>
                            <th>Coin</th>
                            <th>Price</th>
                            <th>24h Change</th>
                            <th>Market Cap</th>
                        </tr>
                        </thead>
                        <tbody>
                        {coins.map(coin => (
                            <tr key={coin.id} onClick={() => navigate(coin.id)}>
                                <td>
                                    <img className="crypto-logo" src={coin.image} alt={coin.name}/>
                                    <div className="crypto-info">
                                        <b>{coin.symbol.toUpperCase()}</b>
                                        <span>{coin.name}</span>
                                    </div>
                                </td>
                                <td>{currencyFormat(coin.current_price)}</td>
                                <td className={coin.price_change_percentage_24h < 0 ? 'text-red' : 'text-green'}>{Math.round(coin.price_change_percentage_24h * 100) / 100}%</td>
                                <td>{unitFormat(coin.market_cap)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )
}