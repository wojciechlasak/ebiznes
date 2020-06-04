import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';

export default function Auth() {
    const { provider } = useParams();

    useEffect(() => {
        window.opener.socialProviderCallback(
            provider,
            new URLSearchParams(window.location.search).toString()
        );
        window.close();
    });

    return <div/>;
}
