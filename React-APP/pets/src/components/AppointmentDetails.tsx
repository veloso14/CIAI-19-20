import React from 'react';
import Container from "react-bootstrap/Container";
import {useParams} from "react-router";
import {GlobalState} from "../App";
import {Appointment} from "./AppointmentList";
import {connect} from "react-redux";
import {getData} from "../Utils/NetworkUtils";
import {Vet} from "./VetList";

const AppointmentDetails = (props: { appointments: Appointment[] }) => {
    let {id} = useParams();
    const [apt, setApt] = React.useState({} as Appointment)

    const [vet, setVet] = React.useState({} as Vet)
    const [loading, setLoading] = React.useState(false)


    const loadAppointment = (id: string) => {
        setLoading(true)
        return getData(`/appointments/${id}`, {} as Appointment)
            .then(data => {
                console.log("fetch appointment: " + JSON.stringify(data))
                setApt(data)
                setLoading(false)
            })
    };


    const loadVet = (id: string) => {
        setLoading(true)
        return getData(`/vets/${+id}`, {} as Vet)
            .then(data => {
                console.log("log: " + JSON.stringify(data))
                data && setVet(data)
                setLoading(false)
            })

    }


    React.useEffect(() => {
        loadAppointment(id as string)
    }, [id])

    React.useEffect(() => {
            if (typeof apt.vetID != "undefined") {
                loadVet(apt.vetID)
            }
        }, [apt]
    )

    let date = String(apt.date)
    let desc = String(apt.desc)

    let content = (
        <div>
            <h1 className="text-center">Appointment Details:</h1>
            <br/>
            <h3>Completed: {desc.length > 0 ? <span>True</span> : <span>False</span>}</h3>
            {desc.length > 0 && <h3>Vet desc: {desc}</h3>}
            <h3>Date: {date.toString().slice(0, 10)}</h3>
            <h3>Time: {date.toString().slice(11, 19)}</h3>
            <h3>Vet: {vet.name}</h3>
        </div>
    )

    let loadingContent = (
        <div>
            <h1>Loading...</h1>
        </div>
    )

    return (
        <Container className="m-4">
            {loading ? loadingContent : content}
        </Container>
    );
}


const mapStateToProps = (state: GlobalState) => {
    return {
        pet: state.pets.pet,
        appointments: state.pets.appointments,
        isFetching: state.pets.isFetching
    }
};
const mapDispatchToProps = (dispatch: any) => {
    return {}
};
export default connect(mapStateToProps, mapDispatchToProps)(AppointmentDetails);