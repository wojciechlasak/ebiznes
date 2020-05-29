import React, { useContext } from 'react';
import { UserContext } from '../providers/UserProvider';
import { BasketContext } from '../providers/BasketProvider';
import { authenticate } from "../services/AuthService";
import { getBasket, checkBasket, createBasket } from "../services/BasketService"

let existingWindow = null;

export default function SocialLoginButton({provider, title}) {
    const { setUser } = useContext(UserContext);
    const { setBasket } = useContext(BasketContext);

    async function handleCreateBasket(user) {
        if(user) {
            let baskets;

            let check = await checkBasket(user.id);
            if(!check) await createBasket(user.id);

            baskets = await getBasket(user.id);
            console.log(baskets)
            await setBasket(
                baskets.filter((basket)=> basket.isOrdered === 0)[0]
            )
        }
    }

    function handleAuthentication() {
        window.socialProviderCallback = async function (socialProvider, queryParams) {
            let user = await authenticate(socialProvider, queryParams);
            await setUser(user);
            await handleCreateBasket(user);
        };

        if (existingWindow) {
            existingWindow.close();
        }

        const w = 500;
        const h = 500;
        const dualScreenLeft =
            window.screenLeft !== undefined ? window.screenLeft : window.screenX;
        const dualScreenTop =
            window.screenTop !== undefined ? window.screenTop : window.screenY;

        const width = window.innerWidth
            ? window.innerWidth
            : document.documentElement.clientWidth
                ? document.documentElement.clientWidth
                : window.screen.width;
        const height = window.innerHeight
            ? window.innerHeight
            : document.documentElement.clientHeight
                ? document.documentElement.clientHeight
                : window.screen.height;

        const systemZoom = width / window.screen.availWidth;
        const left = (width - w) / 2 / systemZoom + dualScreenLeft;
        const top = (height - h) / 2 / systemZoom + dualScreenTop;
        existingWindow = window.open(
            `http://localhost:9000/authenticate/${provider}`,
            'Authentication',
            'scrollbars=yes, width=' +
            w / systemZoom +
            ', height=' +
            h / systemZoom +
            ', top=' +
            top +
            ', left=' +
            left
        );
    }

    return (
        <button
            type="button"
            className="button-base button-menu"
            onClick={handleAuthentication}
        >
            {title}
        </button>
    );
}
