import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {fetchOne} from "../tasks/crypto";
import {currencyFormat, unitFormat} from "../utils/format";
import {pushHistory} from "../tasks/history"
import {useTheme} from "../hooks/theme";

export default function CoinDetails() {
    const [, builder] = useTheme();
    const {coinId} = useParams();
    const [coin, setCoin] = useState(null);
    const [found, setFound] = useState(true);

    useEffect(() => {
        fetchOne(coinId).then(res => {
            setCoin(res);
            pushHistory(res);
        }).catch(err => {
            setFound(false);
        })
    }, [])

    return (
        <div className={builder('coin-details')}>
            {found ? (
                coin ? (<>
                    <img src={coin.image.large} alt={coin.name}/>
                    <h1>{coin.name}</h1>
                    <p dangerouslySetInnerHTML={{__html: coin.description.en}}/>
                    <div><b>Rank: </b>{coin.market_cap_rank}</div>
                    <div><b>Current Price: </b>{currencyFormat(coin.market_data.current_price.usd)}</div>
                    <div><b>Market Cap: </b>{unitFormat(coin.market_data.market_cap.usd)}</div>
                </>) : (<div>Loading ...</div>)
            ) : (<div>
                <h1>Coin not found</h1>
            </div>)}
        </div>
    )
}